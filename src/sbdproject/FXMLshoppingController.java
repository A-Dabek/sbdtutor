package sbdproject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/*
    klasa odpowiedzialna za panel wykupowania dostępu do kursu
*/

public class FXMLshoppingController implements Initializable {

    //kontrolki
    @FXML
    private TextField fld_name;
    @FXML
    private TextField fld_cost;
    @FXML
    private TextField fld_credits;
    @FXML
    private TextField fld_lessons;
    @FXML
    private TextField fld_author;
    
    //kolekcje rekordów
    private ArrayList<Lesson> less = null;
    private ArrayList<UserAccount> users = null;
    
    private Integer cost = null; //koszt całkowity wybranych lekcji
    private Integer lessons = null; //liczba lekcji, którą chcemy
    
    //teść zapytań
    private final String PSuser_fetch = "select * from sbd_tutor.clients"
            + " where name = ?";
    
    private final String PSupdate = "update sbd_tutor.clients"
            + " SET account = account + ? WHERE name = ?";
    
    private final String PSinsert = "insert into sbd_tutor.payments"
            + " values(?, ?, ?, ?, ?, ?)";
    private final String PSlesson_fetch = "select * from sbd_tutor.lesson l"
                                        + " where ordinal > (select ifnull(max(lessons), 0) from sbd_tutor.payments p"
                                        + " where p.course = l.course and p.cat = l.cat and p.user = ?)"
                                        + " and course = ? and cat = ?"
                                        + " and ordinal <= ? order by ordinal desc";
    private final String PSadd_view = "update sbd_tutor.courses set views = views + ?"
            + " where title = ? and cat = ?";
    
    //powrót do panelu wyszukiwania
    @FXML
    private void getBack(ActionEvent event) {
        //wyczyść kontekst i wróć do panelu wyszukiwania
        DBcontrol.context_course = null;
        SBDproject.changeScene(getClass(), "FXMLsearchpanel.fxml", event);
    }
    
    @FXML
    private void buyCourse(ActionEvent event) {
        if(cost <= 0) return;
        //jeśli możemy zakupić lekcje, to odejmij środki z konta
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        args.add(new DBstatement.arg(null, -cost, null));
        args.add(new DBstatement.arg(DBcontrol.context_user.getName(), null, null));
        
        Integer flag = DBstatement.execState(3, args);
        
        if(flag == 0) {
            //jeśli środki udało się pobrać, można przakazać je autorowi
            ArrayList<DBstatement.arg> args2 = new ArrayList<>();
            args2.add(new DBstatement.arg(null, cost, null));
            args2.add(new DBstatement.arg(DBcontrol.context_course.getAuthor(), null, null));
            DBstatement.execState(3, args2);
            
            //dodajemy rekord do tablicy "payment", aby zachować informację o zakupie
            ArrayList<DBstatement.arg> args3 = new ArrayList<>();
            args3.add(new DBstatement.arg(DBcontrol.context_user.getName(), null, null));
            args3.add(new DBstatement.arg(DBcontrol.context_course.getAuthor(), null, null));
            args3.add(new DBstatement.arg(DBcontrol.context_course.getTitle(), null, null));
            args3.add(new DBstatement.arg(DBcontrol.context_course.getCat(), null, null));
            args3.add(new DBstatement.arg(null, lessons, null));
            args3.add(new DBstatement.arg(null, cost, null));
            DBstatement.execState(2, args3);
            
            //oraz zwiększamy liczbę "wyśiwetleń"
            ArrayList<DBstatement.arg> args4 = new ArrayList<>();
            args4.add(new DBstatement.arg(null, 1, null));
            args4.add(new DBstatement.arg(DBcontrol.context_course.getTitle(), null, null));
            args4.add(new DBstatement.arg(DBcontrol.context_course.getCat(), null, null));
            DBstatement.execState(4, args4);
            
            checkPrice(null);
        }
    }
    
    @FXML
    private void checkPrice(ActionEvent event) {
        //sprawdzamy ile lekcji zażądał użytkownik
        String text = fld_lessons.getText();
        Integer val;
        try {
            val = Integer.parseInt(text);
        } catch(NumberFormatException e) {
            System.out.println(e.getMessage());
            val = DBcontrol.context_course.getLessons();
        }
        if(val <= 0 || val > DBcontrol.context_course.getLessons())
                val = DBcontrol.context_course.getLessons();
        
        //wykonujemy zapytanie celem sprawdzenia kosztu tylu lekcji z ewentualnym rabatem
        System.out.println(val);
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        args.add(new DBstatement.arg(DBcontrol.context_user.getName(), null, null));
        args.add(new DBstatement.arg(DBcontrol.context_course.getTitle(), null, null));
        args.add(new DBstatement.arg(DBcontrol.context_course.getCat(), null, null));
        args.add(new DBstatement.arg(null, val, null));
        less = DBstatement.execFetch("lesson", 1, args);
        
        //zliczamy sumę cen z krotek
        Integer sum = 0;
        System.out.println("rozmiar do kupienia " + less.size());
        for (int i =0; i<less.size(); i++) sum += less.get(i).getPrice();
        fld_cost.setText(String.valueOf(sum));
        cost = sum;
        lessons = 0;
        if (less.size() > 0)
            lessons = less.get(0).getOrdinal();
        if (val < lessons)
            lessons = val;
        fld_lessons.setText(String.valueOf(lessons));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fld_name.setEditable(false);
        fld_cost.setEditable(false);
        fld_credits.setEditable(false);
        fld_author.setEditable(false);
        if(DBcontrol.context_course != null && DBcontrol.context_user != null) {
            //jeśli kontekst istnieje to przygotuj zapytania
            DBstatement.initStates();
            DBstatement.prepState(PSuser_fetch);
            DBstatement.prepState(PSlesson_fetch);
            DBstatement.prepState(PSinsert);
            DBstatement.prepState(PSupdate);
            DBstatement.prepState(PSadd_view);
            
            //sprawdź w bazie ile użytkownik ma wolnych środków
            ArrayList<DBstatement.arg> args = new ArrayList<>();
            args.add(new DBstatement.arg(DBcontrol.context_user.getName(), null, null));
            users = DBstatement.execFetch("client", 0, args);
            
            fld_credits.setText(String.valueOf(users.get(0).getAccount()));
            fld_name.setText(DBcontrol.context_course.getTitle());
            fld_author.setText(DBcontrol.context_course.getAuthor());
            fld_lessons.setText(String.valueOf(DBcontrol.context_course.getLessons()));
            checkPrice(null);
        }
    }    
    
}

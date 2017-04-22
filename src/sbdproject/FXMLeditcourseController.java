package sbdproject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/*
    klasa odpowiadająca za przeglądanie i ewenetualną edycję kursu
*/

public class FXMLeditcourseController implements Initializable {
    
    private Boolean authorView = false;
    
    //kontrolki
    @FXML
    private TextField fld_l_title;
    @FXML
    private TextField fld_c_title;
    @FXML
    private TextArea txta_lesson;
    @FXML
    private TextArea txta_course;
    @FXML
    private Label lbl_lesson;
    
    //przeglądanie lekcji wstecz i naprzód
    @FXML
    private void prevLesson(ActionEvent event) {
        if(lesson_ordinal > 0) {
            lesson_ordinal -= 1;
            displayLesson();
        }
    }
    
    @FXML
    private void nextLesson(ActionEvent event) {
        if(lesson_ordinal < lessons.size() - 1) {
            lesson_ordinal += 1;
            if(!authorView && max_bought < lessons.get(lesson_ordinal).getOrdinal())
                lesson_ordinal -= 1;
            else
                displayLesson();
        }
    }
    
    //powrót do panelu przeszukiwania
    @FXML
    private void getBack(ActionEvent event) {
        String c_title = fld_c_title.getText();
        String c_content = txta_course.getText();
        if(c_content.equals("") || c_title.equals("")) return;
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        args.add(new DBstatement.arg(c_title, null, null));
        args.add(new DBstatement.arg(c_content, null, null));
        args.add(new DBstatement.arg(DBcontrol.context_course.getCat(), null, null));
        args.add(new DBstatement.arg(DBcontrol.context_course.getTitle(), null, null));
        DBstatement.execState(5, args);
        DBcontrol.context_course = null;
        SBDproject.changeScene(getClass(), "FXMLsearchpanel.fxml", event);
    }
    
    //aktualizacja treści lekcji
    @FXML
    private void updateLesson(ActionEvent event) {
        if(lessons.size() <= 0) return;
        String c_title = fld_l_title.getText();
        String c_content = txta_lesson.getText();
        Integer c_price = null;
        try {
            c_price = Integer.parseInt(fld_cost.getText());
        } catch(NumberFormatException e) {
            System.out.println(e.getMessage());
            return;
        }
        if(c_content.equals("") || c_title.equals("")) return;
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        args.add(new DBstatement.arg(c_title, null, null));
        args.add(new DBstatement.arg(c_content, null, null));
        args.add(new DBstatement.arg(null, c_price, null));
        
        args.add(new DBstatement.arg(DBcontrol.context_course.getCat(), null, null));
        args.add(new DBstatement.arg(DBcontrol.context_course.getTitle(), null, null));
        args.add(new DBstatement.arg(null, lessons.get(lesson_ordinal).getOrdinal(), null));
        DBstatement.execState(3, args);
        fetchLessons();
    }
    
    //dodanie kolejnej lekcji
    @FXML
    private void insertLesson(ActionEvent event) {
        // if(lessons.size() <= 0) return;
        String c_title = fld_l_title.getText();
        String c_content = txta_lesson.getText();
        Integer c_price = null;
        try {
            c_price = Integer.parseInt(fld_cost.getText());
        } catch(NumberFormatException e) {
            System.out.println(e.getMessage());
            return;
        }
        if(c_content.equals("") || c_title.equals("")) return;
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        args.add(new DBstatement.arg(c_title, null, null));
        args.add(new DBstatement.arg(c_content, null, null));
        Integer insert_ordinal = lesson_ordinal + 1;
        for(int i = lesson_ordinal; i < lessons.size(); i++) {
            if(!Objects.equals(lessons.get(i).getOrdinal(), insert_ordinal)) {
                break;
            }
            insert_ordinal += 1;
        }
        
        args.add(new DBstatement.arg(null, insert_ordinal, null));
        args.add(new DBstatement.arg(null, c_price, null));
        args.add(new DBstatement.arg(DBcontrol.context_course.getTitle(), null, null));
        args.add(new DBstatement.arg(DBcontrol.context_course.getCat(), null, null));
        DBstatement.execState(2, args);
        fetchLessons();
        displayLesson();
    }
    
    //usunięcie obecnej lekcji
    @FXML
    private void deleteLesson(ActionEvent event) {
        if(lessons.size() <= 0) return;
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        args.add(new DBstatement.arg(DBcontrol.context_course.getCat(), null, null));
        args.add(new DBstatement.arg(DBcontrol.context_course.getTitle(), null, null));
        args.add(new DBstatement.arg(null, lessons.get(lesson_ordinal).getOrdinal(), null));
        DBstatement.execState(1, args);
        fetchLessons();
        displayLesson();
    }
    
    //pobranie lekcji
    private void fetchLessons() {
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        args.add(new DBstatement.arg(DBcontrol.context_course.getCat(), null, null));
        args.add(new DBstatement.arg(DBcontrol.context_course.getTitle(), null, null));
        lessons = DBstatement.execFetch("lesson", 0, args);
    }
    
    //wyświetlanie lekcji
    private Integer lesson_ordinal = 0;
    private Integer max_bought = 0;
    private void displayLesson() {
        System.out.println("size " + lessons.size());
        System.out.println("max_bought " + max_bought);
        
        if(lessons == null) return;
        if(lesson_ordinal < 0 || lesson_ordinal >= lessons.size()) return;
        Lesson temp = lessons.get(lesson_ordinal);
        if(temp == null) return;
        if(authorView) {
            fld_cost.setText(String.valueOf(temp.getPrice()));
        }
        txta_lesson.setText(temp.getContent());
        fld_l_title.setText(temp.getTitle());
        lbl_lesson.setText("Lesson " + temp.getOrdinal());
    }
    
    //kontrolki
    @FXML
    private Button btn_insert;
    @FXML
    private Button btn_delete;
    @FXML
    private Button btn_update;
    @FXML
    private TextField fld_cost;
    @FXML
    private Label lbl_cost;
    
    //zapytania
    private ArrayList<Lesson> lessons = null;
    private final String PSfetch = "select * from sbd_tutor.lesson"
            + " where cat = ? and course = ? order by ordinal asc";
    private final String PSupdate = "update sbd_tutor.lesson set title = ?, content = ?, price = ?"
            + " where cat = ? and course = ? and ordinal = ?";
    private final String PSdelete = "delete from sbd_tutor.lesson where"
            + " cat = ? and course = ? and ordinal = ?";
    private final String PSinsert = "insert into sbd_tutor.lesson(title, content, ordinal, price, course, cat) values"
            + "(?, ?, ?, ?, ?, ?)";
    private final String PSmax = "select * from sbd_tutor.payments"
            + " where user =? and cat=? and course=? order by lessons desc";
    private final String PSupdate_course = "update sbd_tutor.courses"
            + " set title = ?, description = ? where cat = ? and title = ?";
    
    //przygotowanie na podstawie kontekstu (czytelnik bądź edytor)
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBstatement.initStates();
        DBstatement.prepState(PSfetch);
        DBstatement.prepState(PSdelete);
        DBstatement.prepState(PSinsert);
        DBstatement.prepState(PSupdate);
        DBstatement.prepState(PSmax);
        DBstatement.prepState(PSupdate_course);
        if(DBcontrol.context_course != null && DBcontrol.context_user != null) {
            fld_c_title.setText(DBcontrol.context_course.getTitle());
            txta_course.setText(DBcontrol.context_course.getDescritpion());
            if(DBcontrol.context_course.getAuthor().equals(DBcontrol.context_user.getName())) {
                authorView = true;
            } else {
                btn_insert.setVisible(false);
                btn_update.setVisible(false);
                btn_delete.setVisible(false);
                fld_cost.setVisible(false);
                lbl_cost.setVisible(false);
                txta_lesson.setEditable(false);
                fld_l_title.setEditable(false);
                txta_course.setEditable(false);
                fld_c_title.setEditable(false);
            }
            fetchLessons();

            ArrayList<DBstatement.arg> args2 = new ArrayList<>();
            args2.add(new DBstatement.arg(DBcontrol.context_user.getName(), null, null));
            args2.add(new DBstatement.arg(DBcontrol.context_course.getCat(), null, null));
            args2.add(new DBstatement.arg(DBcontrol.context_course.getTitle(), null, null));
            ArrayList<Payment> pmt = DBstatement.execFetch("payment", 4, args2);
            max_bought = DBcontrol.context_course.getLessons();
            displayLesson();
        }
    }    
    
}

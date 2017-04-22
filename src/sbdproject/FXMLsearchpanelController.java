package sbdproject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/*
    klasa odpowiedzialna za panel wyszukiwania kursów
*/

public class FXMLsearchpanelController implements Initializable {
    
    //kontrolki
    @FXML
    private ListView lv_course;
    @FXML
    private ListView lv_cat;
    @FXML
    private TextArea txta_overvw;
    @FXML
    private TextField fld_title;
    @FXML
    private Button btn_create;
    @FXML
    private Button btn_enter;
    @FXML
    private Button btn_access;
    
    //kolekcje rekordów
    private ArrayList<Category> cats;
    private ArrayList<Course> courses;
    private String cat_name = "";
    private String cat_parent = "";
    private Course course_toBePassed = null;
    
    //zapytania
    private final String PScat_fetch = "select * from sbd_tutor.cats"
                                    + " where parent = ?";
    private final String PScourse_fetch = "select * from sbd_tutor.courses"
                                    + " where cat = ?";
    private final String PSinsert = "insert into sbd_tutor.courses"
            + " values(?, 'empty description', ?, ?, 15, 0, 0)";
    private final String PSpayment = "select * from sbd_tutor.payments"
            + " where user = ? and author = ? and course = ? and cat = ? order by lessons desc";
    
    @FXML
    private void createCourse(ActionEvent event) {
        String selectedItem = (String) lv_cat.getSelectionModel().getSelectedItem();
        if(selectedItem == null) selectedItem = "";
        //zaznaczony rodzic to kategoria naszych kursów
        cat_name = selectedItem;
        //sprawdzamy czy podano tytuł kursu
        String c_title = fld_title.getText();
        if(c_title.equals("")) return;
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        args.add(new DBstatement.arg(c_title, null, null));
        args.add(new DBstatement.arg(DBcontrol.context_user.getName(), null, null));
        args.add(new DBstatement.arg(cat_name, null, null));
        Integer flag = DBstatement.execState(2, args);
        
        DBcontrol.context_course = new Course();
        DBcontrol.context_course.setDescription("empty description");
        DBcontrol.context_course.setTitle(c_title);
        DBcontrol.context_course.setCat(cat_name);
        DBcontrol.context_course.setLessons(1);
        DBcontrol.context_course.setAuthor(DBcontrol.context_user.getName());
        
        if(flag == 0) {
            SBDproject.changeScene(getClass(), "FXMLeditcourse.fxml", event);
        }
    }
            
    @FXML
    private void signOut(ActionEvent event) {
        DBstatement.closeStates();
        //wyczyść kontekst, rozłącz się z bazą i wróć do panelu logowania
        DBcontrol.context_course = null;
        DBcontrol.context_user = null;
        DBcontrol.dbDisconnect();
        SBDproject.changeScene(getClass(), "FXMLsignin.fxml", event);
    }
    
    @FXML
    private void enterCourse(ActionEvent event) {
        if(course_toBePassed == null) return;
        ArrayList<Payment> pays;
        
        //wykonanie zapytania i pobranie rekordów
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        args.add(new DBstatement.arg(DBcontrol.context_user.getName(), null, null));
        args.add(new DBstatement.arg(course_toBePassed.getAuthor(), null, null));
        args.add(new DBstatement.arg(course_toBePassed.getTitle(), null, null));
        args.add(new DBstatement.arg(course_toBePassed.getCat(), null, null));
        pays = DBstatement.execFetch("payment", 3, args);
        
        if(pays.size() > 0 || course_toBePassed.getAuthor().equals(DBcontrol.context_user.getName())) {
            DBstatement.closeStates();
            //stwórz kontekst i idź do panelu przeglądania kursu i lekcji
            DBcontrol.context_course = new Course();
            DBcontrol.context_course.setDescription(course_toBePassed.getDescritpion());
            DBcontrol.context_course.setTitle(course_toBePassed.getTitle());
            DBcontrol.context_course.setCat(course_toBePassed.getCat());
            if(pays.size() <= 0) 
                DBcontrol.context_course.setLessons(course_toBePassed.getLessons());
            else
                DBcontrol.context_course.setLessons(pays.get(0).getLessons());
            DBcontrol.context_course.setAuthor(course_toBePassed.getAuthor());
            SBDproject.changeScene(getClass(), "FXMLeditcourse.fxml", event);
        }
    }
    
    @FXML
    private void showUserAccount(ActionEvent event) {
        //idź do panelu konta użytkownika
        DBstatement.closeStates();
        SBDproject.changeScene(getClass(), "FXMLuseraccount.fxml", event);
    }
    
    @FXML
    private void showSubcats(ActionEvent event) {
        //sprawdź zaznaczony obiekt
        String selectedItem = (String) lv_cat.getSelectionModel().getSelectedItem();
        if(selectedItem == null) selectedItem = "";
        //naszym rodzicem będzie zaznaczony obiekt bądź "" czyli root
        cat_parent = selectedItem;
        ObservableList<String> items = FXCollections.observableArrayList();
        //wykonujemy zapytanie
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        args.add(new DBstatement.arg(cat_parent, null, null));
        cats = DBstatement.execFetch("cat", 0, args);
        //wypełniamy kontrolkę danymi
        cats.forEach((u) -> {
            items.add(u.getName());
        });
        lv_cat.setItems(items);
    }
    
    @FXML
    private void showCourselist(ActionEvent event) {
        String selectedItem = (String) lv_cat.getSelectionModel().getSelectedItem();
        if(selectedItem == null) selectedItem = "";
        //zaznaczony rodzic to kategoria naszych kursów
        cat_name = selectedItem;
        ObservableList<String> items = FXCollections.observableArrayList();
        
        //wykonanie zapytania i pobranie rekordów
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        args.add(new DBstatement.arg(cat_name, null, null));
        courses = DBstatement.execFetch("course", 1, args);
        
        Collections.sort(courses, (Course c2, Course c1) -> c1.getViews().compareTo(c2.getViews()));
     
        courses.forEach((u) -> {
            items.add(u.getTitle() + " (" + String.valueOf(u.getViews()) + ")");
        });
        
        lv_course.setItems(items);
    }
    
    @FXML
    private void showCourseoverview(ActionEvent event) {
        Integer selectedItem = lv_course.getSelectionModel().getSelectedIndex();
        if(courses == null) return;
        if(selectedItem < courses.size() && selectedItem >= 0) {
            //jeśli wybraliśmy kurs z listy to pokaż dokładny opis
            String text = courses.get(selectedItem).getTitle().toUpperCase() + "\n\n"
                    + "by: " + courses.get(selectedItem).getAuthor() + "\n"
                    + courses.get(selectedItem).getLessons() +" lessons\n\n"
                    + courses.get(selectedItem).getDescritpion();
            txta_overvw.setText(text);
            course_toBePassed = courses.get(selectedItem);
        } else {
            txta_overvw.setText("");
            course_toBePassed = null;
        }
    }
    
    //przygotowanie kontekstu i wejście w tryb oglądania kursu
    @FXML
    private void getAccess(ActionEvent event) {
        if(course_toBePassed == null) return;
        DBcontrol.context_course = new Course();
        DBcontrol.context_course.setTitle(course_toBePassed.getTitle());
        DBcontrol.context_course.setCat(course_toBePassed.getCat());
        DBcontrol.context_course.setLessons(course_toBePassed.getLessons());
        DBcontrol.context_course.setAuthor(course_toBePassed.getAuthor());
        SBDproject.changeScene(getClass(), "FXMLshopping.fxml", event);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txta_overvw.setEditable(false);
        DBstatement.initStates();
        DBstatement.prepState(PScat_fetch);
        DBstatement.prepState(PScourse_fetch);
        DBstatement.prepState(PSinsert);
        DBstatement.prepState(PSpayment);
        if(!(DBcontrol.context_user.isContributor() || DBcontrol.context_user.isElevated())) {
            btn_create.setDisable(true);
            fld_title.setEditable(true);
        }
        if(!DBcontrol.context_user.isAccepted()) {
            btn_enter.setDisable(true);
            btn_access.setDisable(true);
        }
    }    
    
}

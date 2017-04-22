package sbdproject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/*
    klasa odpowiadająca za panel administratora do edytowania rekordów w bazie
*/


public class FXMLadminpanelController implements Initializable {
    
    //kontrolki
    @FXML
    private RadioButton rb_c_ok;
    @FXML
    private RadioButton rb_c_pen;
    @FXML
    private RadioButton rb_reports;
    @FXML
    private RadioButton rb_cat;
    @FXML
    private RadioButton rb_newbs;
    @FXML
    private RadioButton rb_accept;
    @FXML
    private RadioButton rb_tutors;
    @FXML
    private ListView lv_list;
    @FXML
    private TextField fld_catname;
    @FXML
    private TextField fld_name;
    
    private ArrayList<UserAccount> users;
    private ArrayList<Category> cats;
    private ArrayList<Course> courses;
    private String selectedItem = "";
    private String cat_name = "";
    private String cat_parent = "";
    
    private final String cat_fetch = "select * from sbd_tutor.cats"
                                    + " where parent = ?";
    private final String cat_update = "update sbd_tutor.cats"
                                    + " SET name = ? WHERE name = ?";
    private final String cat_delete = "delete sbd_tutor.cats WHERE name = ?";
    private final String cat_insert = "insert into sbd_tutor.cats"
                                    + " values(?, ?)";
    private final String user_delete = "delete from sbd_tutor.clients"
                                    + " where name = ?";
    private final String user_fetch = "select * from sbd_tutor.clients"
                                    + " where accepted = ? and tutor = ?";
    private final String user_update = "update sbd_tutor.clients"
                                    + " SET accepted = ?, tutor = ? WHERE name = ?";
    
    private final String course_delete = "delete from sbd_tutor.courses"
                                    + " where cat = ? and title = ?";
    private final String course_fetch = "select * from sbd_tutor.courses"
                                    + " where cat = ? and accepted = ?";
    private final String course_update = "update sbd_tutor.courses"
                                    + " SET cat = ?, accepted = ? WHERE cat = ? and title = ?";
    private final String course_rename = "update sbd_tutor.courses"
                                    + " SET title = ? WHERE cat = ? and title = ?";
    
    @FXML
    private void switchScene(ActionEvent event) {
        SBDproject.changeScene(getClass(), "FXMLsearchpanel.fxml", event);
    }
    
    private void refreshData() {
        ObservableList<String> items = FXCollections.observableArrayList();
        if(cats != null) {
            cats.forEach((u) -> {
                items.add(u.getName());
            });
            
        } else if(users != null) {
            users.forEach((u) -> {
                items.add(u.getName());
            });
            
        } else if(courses != null) {
            courses.forEach((u) -> {
                items.add(u.getTitle());
            });
            
        }
        lv_list.setItems(items);
    }
    
    @FXML
    private void updateButton(ActionEvent event) {
        selectedItem = (String) lv_list.getSelectionModel().getSelectedItem();
        if(selectedItem == null) return;
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        
        if(rb_c_ok.isSelected()) {
                if(fld_name.getText().equals("")) return;
                args.add(new DBstatement.arg(fld_name.getText(), null, null));
                args.add(new DBstatement.arg(cat_name, null, null));
                args.add(new DBstatement.arg(selectedItem, null, null));
                DBstatement.execState(10, args);
            
        } else if(rb_c_pen.isSelected()) {
                args.add(new DBstatement.arg(cat_name, null, null));
                args.add(new DBstatement.arg(null, null, Boolean.TRUE));
                args.add(new DBstatement.arg(cat_name, null, null));
                args.add(new DBstatement.arg(selectedItem, null, null));
                DBstatement.execState(9, args);
            
        } else if(rb_reports.isSelected()) {
            
        } else if(rb_cat.isSelected()) {
                cat_name = fld_name.getText();
                args.add(new DBstatement.arg(cat_name, null, null));
                args.add(new DBstatement.arg(selectedItem, null, null));
                DBstatement.execState(3, args);
                selectedItem = cat_parent;
        } else {
            if(rb_newbs.isSelected()) {
                args.add(new DBstatement.arg(null, null, Boolean.TRUE));
                args.add(new DBstatement.arg(null, null, Boolean.FALSE));
            }
            else if(rb_accept.isSelected()) {
                args.add(new DBstatement.arg(null, null, Boolean.TRUE));
                args.add(new DBstatement.arg(null, null, Boolean.TRUE));
            } else return;
            args.add(new DBstatement.arg(selectedItem, null, null));
            DBstatement.execState(6, args);
        }
    }
    
    @FXML
    private void fetchButton(ActionEvent event) {
        selectedItem = (String) lv_list.getSelectionModel().getSelectedItem();
        if(selectedItem == null) selectedItem = "";
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        cats = null;
        users = null;
        
        if(rb_c_ok.isSelected()) {
                cat_name = fld_catname.getText();
                args.add(new DBstatement.arg(cat_name, null, null));
                args.add(new DBstatement.arg(null, null, Boolean.TRUE));
                courses = DBstatement.execFetch("course", 7, args);
            
        } else if(rb_c_pen.isSelected()) {
                cat_name = fld_catname.getText();
                args.add(new DBstatement.arg(cat_name, null, null));
                args.add(new DBstatement.arg(null, null, Boolean.FALSE));
                courses = DBstatement.execFetch("course", 7, args);
            
        } else if(rb_reports.isSelected()) {
            
        } else if(rb_cat.isSelected()) {
            cat_parent = selectedItem;
            args.add(new DBstatement.arg(cat_parent, null, null));
            cats = DBstatement.execFetch("cat", 0, args);
        } else {
            if(rb_newbs.isSelected()) {
                args.add(new DBstatement.arg(null, null, Boolean.FALSE));
                args.add(new DBstatement.arg(null, null, Boolean.FALSE));
            }
            if(rb_accept.isSelected()) {
                args.add(new DBstatement.arg(null, null, Boolean.TRUE));
                args.add(new DBstatement.arg(null, null, Boolean.FALSE));
            }
            if(rb_tutors.isSelected()) {
                args.add(new DBstatement.arg(null, null, Boolean.TRUE));
                args.add(new DBstatement.arg(null, null, Boolean.TRUE));
            }
            users = DBstatement.execFetch("client", 4, args);
        }
        refreshData();
    }
    
    @FXML
    private void insertButton(ActionEvent event) {
        selectedItem = (String) lv_list.getSelectionModel().getSelectedItem();
        if(selectedItem == null) selectedItem = "";
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        
        if(rb_c_ok.isSelected()) {
            
        } else if(rb_c_pen.isSelected()) {
            
            
        } else if(rb_reports.isSelected()) {
            
        } else if(rb_cat.isSelected()) {
            cat_name = fld_name.getText();
            if(cat_name.equals("")) return;
            args.add(new DBstatement.arg(cat_name, null, null));
            args.add(new DBstatement.arg(cat_parent, null, null));
            DBstatement.execState(2, args);
        }
    }
    
    @FXML
    private void deleteButton(ActionEvent event) {
        selectedItem = (String) lv_list.getSelectionModel().getSelectedItem();
        if(selectedItem == null) return;
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        
        if(rb_c_ok.isSelected()) {
                args.add(new DBstatement.arg(cat_name, null, null));
                args.add(new DBstatement.arg(null, null, Boolean.FALSE));
                args.add(new DBstatement.arg(cat_name, null, null));
                args.add(new DBstatement.arg(selectedItem, null, null));
                DBstatement.execState(9, args);
            
        } else if(rb_c_pen.isSelected()) {
                args.add(new DBstatement.arg(cat_name, null, null));
                args.add(new DBstatement.arg(selectedItem, null, null));
                DBstatement.execState(8, args);
            
        } else if(rb_reports.isSelected()) {
            
        } else if(rb_cat.isSelected()) {
            args.add(new DBstatement.arg(selectedItem, null, null));
            DBstatement.execState(1, args);
        } else {
            if(rb_newbs.isSelected()) {
                args.add(new DBstatement.arg(selectedItem, null, null));
                DBstatement.execState(5, args);
            }
            if(rb_accept.isSelected()) {
                args.add(new DBstatement.arg(null, null, Boolean.FALSE));
                args.add(new DBstatement.arg(null, null, Boolean.FALSE));
                args.add(new DBstatement.arg(selectedItem, null, null));
                DBstatement.execState(6, args);
            }
            if(rb_tutors.isSelected()) {
                args.add(new DBstatement.arg(null, null, Boolean.TRUE));
                args.add(new DBstatement.arg(null, null, Boolean.FALSE));
                args.add(new DBstatement.arg(selectedItem, null, null));
                DBstatement.execState(6, args);
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBstatement.initStates();
        DBstatement.prepState(cat_fetch);
        DBstatement.prepState(cat_delete);
        DBstatement.prepState(cat_insert);
        DBstatement.prepState(cat_update);
        
        DBstatement.prepState(user_fetch);
        DBstatement.prepState(user_delete);
        DBstatement.prepState(user_update);
        
        DBstatement.prepState(course_fetch);
        DBstatement.prepState(course_delete);
        DBstatement.prepState(course_update);
        DBstatement.prepState(course_rename);
    }    
    
}

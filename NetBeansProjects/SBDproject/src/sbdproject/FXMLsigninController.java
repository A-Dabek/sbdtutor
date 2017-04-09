package sbdproject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

/*
    klasa odpowiadająca za panel logowania do serwisu
*/

public class FXMLsigninController implements Initializable {
    
    //kontrolki
    @FXML
    private TextField fld_user;
    @FXML
    private PasswordField fld_pass;
    @FXML
    private Label lbl_msg;
    
    //treść zapytań
    private final String PSinsert = "insert into sbd_tutor.clients"
                            + "(name, password, account, accepted, tutor, elevated)"
                            + " values(?, ?, 0, FALSE, FALSE, FALSE)";
    private final String PSfetch = "select * from sbd_tutor.clients"
                            + " where name = ? and password = ?";
    
    //reakcja na wciśnięcie "register"
    @FXML
    private void registerUser(ActionEvent event) {
        String name = fld_user.getText();
        String pass = fld_pass.getText();
        if("".equals(name) || "".equals(pass)) {
            lbl_msg.setText("Fill all blanks!");
        } else {
            //przygotowanie argumentów zapytania do rejestracji
            ArrayList<DBstatement.arg> args = new ArrayList<>();
            args.add(new DBstatement.arg(name,null,null));
            args.add(new DBstatement.arg(pass,null,null));
            Integer flag = DBstatement.execState(0, args);
            
            if(flag==0) lbl_msg.setText("New account registered!");
            else lbl_msg.setText("This name is already taken");    
        }
    }
    
    //reakcja na wciśnięcie "log in"
    @FXML
    private void loginUser(ActionEvent event) {
        String name = fld_user.getText();
        String pass = fld_pass.getText();
        if("".equals(name) || "".equals(pass)) {
            lbl_msg.setText("Fill all blanks!");
        } else {
            
            //przygotowanie argumentów zapytania do logowania
            ArrayList<DBstatement.arg> args = new ArrayList<>();
            args.add(new DBstatement.arg(name,null,null));
            args.add(new DBstatement.arg(pass,null,null));
            ArrayList<UserAccount> users = DBstatement.execFetch("client", 1, args);
            
            //jeśli mamy jakiekolwiek krotki z zapytania, to
            if(users.size() > 0) {
                //ustawiamy zmienną kontekstową, aby wiedzieć jaki użytkownik się zalogował
                DBcontrol.context_user = new UserAccount();
                DBcontrol.context_user.setName(users.get(0).getName());
                DBcontrol.context_user.setAccount(users.get(0).getAccount());
                DBcontrol.context_user.isAccepted(users.get(0).isAccepted());
                DBcontrol.context_user.isContributor(users.get(0).isContributor());
                DBcontrol.context_user.isElevated(users.get(0).isElevated());
                //zamykamy zapytania
                DBstatement.closeStates();
                if(users.get(0).isElevated())
                {
                    //jeśli użytkownik ma prawa administratora, to przeloguj go na jego konto w bazie danych
                    DBcontrol.dbDisconnect();
                    DBcontrol.dbConnect(name, pass);
                    SBDproject.changeScene(getClass(), "FXMLadminpanel.fxml", event);
                } else if(users.get(0).isContributor()) {
                    DBcontrol.dbDisconnect();
                    DBcontrol.dbConnect("tutor", "imtutor");
                    SBDproject.changeScene(getClass(), "FXMLsearchpanel.fxml", event);
                }
                else
                    SBDproject.changeScene(getClass(), "FXMLsearchpanel.fxml", event);
            } else lbl_msg.setText("Account not found");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //połączenie z bazą jako gość, celem wykonania zapytań
        DBcontrol.dbConnect("guest", "");
        //wyczyszczenie bazy
        DBstatement.initStates();
        //przygotowanie zapytań
        DBstatement.prepState(PSinsert);
        DBstatement.prepState(PSfetch);
    }    
    
}

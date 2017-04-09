/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sbdproject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/*
    klasa odpowiedzialna za panel użytkownika
 */

public class FXMLuseraccountController implements Initializable {
    
    //kontrolki
    @FXML
    private Label lbl_name;
    @FXML
    private Label lbl_status;
    @FXML
    private TextField fld_coins;
    
    //zapytanie
    private final String PSupdate = "update sbd_tutor.clients"
            + " SET account = account + ? WHERE name = ?";
    
    //zakup dodatkowej ilości tokenów 
    @FXML
    private void getCoins(ActionEvent event) {
        DBcontrol.context_user.setAccount(DBcontrol.context_user.getAccount() + 100);
        fld_coins.setText(String.valueOf(DBcontrol.context_user.getAccount()));
        ArrayList<DBstatement.arg> args = new ArrayList<>();
        args.add(new DBstatement.arg(null, 100, null));
        args.add(new DBstatement.arg(DBcontrol.context_user.getName(), null, null));
        DBstatement.execState(0, args);
    }
    
    //przejście do panelu przeszukiwania kursóœ
    @FXML
    private void goToSearch(ActionEvent event) {
        DBcontrol.context_course = null;
        DBstatement.closeStates();
        SBDproject.changeScene(getClass(), "FXMLsearchpanel.fxml", event);
    }
    
    //wylogowanie
    @FXML
    private void signOut(ActionEvent event) {
        //wyczyść kontekst i wróć do panelu wyszukiwania
        DBcontrol.context_course = null;
        DBcontrol.context_user = null;
        DBstatement.closeStates();
        SBDproject.changeScene(getClass(), "FXMLsignin.fxml", event);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(DBcontrol.context_user != null) {
            lbl_name.setText(DBcontrol.context_user.getName());
            if(DBcontrol.context_user.isElevated()) {
                lbl_status.setText("Admin privilege");
            } else if(DBcontrol.context_user.isContributor()) {
                lbl_status.setText("Tutor privilege");
            } else if(DBcontrol.context_user.isAccepted()) {
                lbl_status.setText("Accepted user");
            } else {
                lbl_status.setText("Unregistered user");
            }
            DBstatement.initStates();
            DBstatement.prepState(PSupdate);
            fld_coins.setEditable(false);
            fld_coins.setText(String.valueOf(DBcontrol.context_user.getAccount()));
        }
    }    
    
}

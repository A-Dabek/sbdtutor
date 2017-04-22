package sbdproject;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/*
    klasa odpowiadająca za panel wystawiania komentarzy i wysyłania zgłoszeń
*/

public class FXMLreportController implements Initializable {

    @FXML
    private RadioButton rb_report;
    @FXML
    private RadioButton rb_one;
    @FXML
    private RadioButton rb_two;
    @FXML
    private RadioButton rb_three;
    @FXML
    private RadioButton rb_four;
    @FXML
    private RadioButton rb_five;
    @FXML
    private TextArea txta_report;
    
    @FXML
    private void sendReport(ActionEvent event) {
        
    }
    @FXML
    private void getBack(ActionEvent event) {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}

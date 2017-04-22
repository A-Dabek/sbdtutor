package sbdproject;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
/*
    główna klasa programu
*/
public class SBDproject extends Application {
    
    
    private Stage primaryStage;
    private AnchorPane rootLayout;
    
    //statyczna funkcja zmieniająca scenę programu w dowolnym momencie wykonania
    public static void changeScene(Class context, String XML, ActionEvent event) {
        DBstatement.closeStates();
        Parent root;
        try {
            root = FXMLLoader.load(context.getResource(XML));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(FXMLsigninController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.primaryStage.setTitle("SBD Tutor");
        initRootLayout();
    }
    
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLsignin.fxml"));
            rootLayout = (AnchorPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}

        

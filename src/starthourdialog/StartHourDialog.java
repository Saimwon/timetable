/*
Simon Van Braeckel
 */
package starthourdialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import timetable.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartHourDialog extends Stage {

    /*
    moet kunnen:
    getresult
    de fxml koppelen aan zichzelf
     */
    private boolean saved;
    private List<String> result;
    private StartHourDialogController controller;

    public StartHourDialog() { //Programma moet crashen als loader.load een exception geeft
        this.saved = false;
        this.result = null;
        //koppel fxml aan zelf
        FXMLLoader loader = new FXMLLoader(getClass().getResource("starthourdialog.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        this.setTitle("Timetable");

        Scene scene = new Scene(root);
        this.setScene(scene);

        this.controller = loader.getController();
        controller.setStartHourDialog(this);
    }

    public List<String> getResult(){
        return result;
    }


}

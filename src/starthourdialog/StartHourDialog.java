/*
Simon Van Braeckel
 */
package starthourdialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class StartHourDialog extends Stage {

    /*
    moet kunnen:
    getresult
    de fxml koppelen aan zichzelf
     */

    private List<String> result;

    public StartHourDialog() throws Exception {
        this.result = new ArrayList<>();
        //koppel fxml aan zelf

        FXMLLoader loader = new FXMLLoader(getClass().getResource("starthourdialog.fxml"));

        Parent root = loader.load();
        this.setTitle("Timetable");

        Scene scene = new Scene(root);
        this.setScene(scene);

        //this.controller = loader.getController();
    }




}

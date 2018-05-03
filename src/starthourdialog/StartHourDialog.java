/*
Simon Van Braeckel
 */
package starthourdialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class StartHourDialog extends Stage {
    private List<Integer[]> startHours;

    public StartHourDialog() {
        this.startHours = null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("starthourdialog.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e){
            //Crash als het programma de fxml file van deze klasse niet vindt
            e.printStackTrace();
            System.exit(1);
        }
        this.setTitle("Timetable");

        Scene scene = new Scene(root);
        this.setScene(scene);

        StartHourDialogController controller = loader.getController();
        controller.setStartHourDialog(this);
    }


    public void setStartHours(List<Integer[]> startHours){
        this.startHours = startHours;
    }

    public List<Integer[]> getStartHours(){
        return this.startHours;
    }
}

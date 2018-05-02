/*
Simon Van Braeckel
 */

package timetable;

import javafx.scene.control.Alert;
import javafx.stage.Window;

import java.util.concurrent.TimeUnit;

public class ErrorDialog extends Alert {
    public ErrorDialog(String message, Window parent){
        super(Alert.AlertType.ERROR);
        this.setTitle("Error.");
        this.setContentText(message);
        this.initOwner(parent);
    }
}

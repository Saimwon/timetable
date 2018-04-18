/*
Simon Van Braeckel
 */

package starthourdialog;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class StartHourDialogController {
    private List<TextField> textFields;
    private StartHourDialog startHourDialog;
    public VBox textFieldContainer;

    public void initialize(){
        textFields = new ArrayList<>();
        addTextField();
    }

    public void addTextField(){
        TextField newTextField = new TextField();
        textFieldContainer.getChildren().add(newTextField);
        if (startHourDialog != null) { //want dit is null wanneer de methode in initialize wordt opgeroepen.
            startHourDialog.sizeToScene();
        }
        textFields.add(newTextField);
    }

    public void setStartHourDialog(StartHourDialog startHourDialog){
        this.startHourDialog = startHourDialog;
    }

    public void save(){
        //string inlezen van textfields en in lijst zetten
        //lijst sorteren
        //lijst in textfields steken
        //lijst in model zetten
    }

    public void cancel(){
        startHourDialog.close();
    }
}

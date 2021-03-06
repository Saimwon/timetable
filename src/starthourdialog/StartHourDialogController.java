/*
Simon Van Braeckel
 */

package starthourdialog;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class StartHourDialogController {
    private StartHourDialog startHourDialog;
    private List<TextField[]> times;
    public VBox inputLineContainer;

    public void initialize(){
        times = new ArrayList<>();
        addTextField();
    }

    /*
    Methode die een nieuwe lijn voor input van een startuur teruggeeft.
     */
    private HBox newInputFields(){
        HBox fieldContainer = new HBox();
        TextField hourField = new TextField();
        TextField minuteField = new TextField();
        Label dubbelpuntLabel = new Label(":");

        //Zet de textfields samen in een lijst zodat we ze bij het saven gemakkelijk kunnen overlopen
        TextField[] textFields = new TextField[]{hourField, minuteField};
        times.add(textFields);

        Button removeButton = new Button("-");
        removeButton.setOnAction(event -> remove(fieldContainer, textFields));

        hourField.setPrefWidth(35.0);
        minuteField.setPrefWidth(35.0);
        fieldContainer.getChildren().addAll(hourField, dubbelpuntLabel, minuteField, removeButton);
        fieldContainer.setAlignment(Pos.CENTER);
        for (Node child : fieldContainer.getChildren()){
            HBox.setMargin(child, new Insets(4,4,4,4));
        }

        return fieldContainer;
    }

    /*
    Verwijder inputlijn van GUI en uit lijst van arrays van textfields die overlopen wordt wanneer men op save drukt.
     */
    private void remove(HBox container, TextField[] textFields){
        inputLineContainer.getChildren().remove(container);
        times.remove(textFields);
    }

    /*
    Voegt nieuwe inputlijn toe.
     */
    public void addTextField(){
        HBox fieldContainer = newInputFields();
        inputLineContainer.getChildren().add(fieldContainer);
        if (startHourDialog != null) { //want dit is null wanneer de methode in initialize wordt opgeroepen.
            startHourDialog.sizeToScene();
        }
    }

    public void setStartHourDialog(StartHourDialog startHourDialog){
        this.startHourDialog = startHourDialog;
    }

    /*
    Methode die alle inputvelden overloopt en checkt of ze geldig zijn.
    Als dat niet het geval is wordt dat duidelijk gemaakt in de GUI
     */
    public void save(){
        List<Integer[]> result = new ArrayList<>();

        boolean allValid = true;
        for (TextField[] textFieldArray : times){
            boolean hour = true;
            for (TextField textField : textFieldArray){
                if (! isgeldig(textField.getCharacters(), hour)){
                    allValid = false;
                    textField.getStyleClass().add("incorrectinput");
                } else {
                    textField.getStyleClass().removeAll("incorrectinput");
                }
                hour = !hour;
            }
            //Als we 1 ongeldig tijdstip zijn tegengekomen is wat hieronder staat niet meer nodig want de methode gaat toch niets teruggeven. => daarom in een if
            if (allValid){
                Integer[] startTijd = new Integer[]{Integer.valueOf(textFieldArray[0].getCharacters().toString()), Integer.valueOf(textFieldArray[1].getCharacters().toString())};
                result.add(startTijd);
            }
        }
        if (! allValid){
            return;
        }

        //Sorteer lijst van starttijden oplopend voor het teruggeven
        result.sort((o1, o2) -> {
            if (o1[0].equals(o2[0])){
                return o1[1] - o2[1];
            } else {
                return o1[0] - o2[0];
            }
        });

        startHourDialog.setStartHours(result);
        startHourDialog.close();
    }

    /*
    Checkt van 1 veld of de ino
     */
    private boolean isgeldig(CharSequence content, boolean hour){
        if (! content.chars().allMatch(Character::isDigit) || content.length() == 0){ // als er een niet-cijfer in zit of de string leeg is
            return false;
        }

        int max = hour ? 23 : 59; //Wordt 24 als param een uur is, anders 60

        return (Integer.parseInt(content.toString()) <= max);
    }

    public void cancel(){
        startHourDialog.close();
    }
}

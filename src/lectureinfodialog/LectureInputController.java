/*
Simon Van Braeckel

Controller voor het venster dat zich opent wanneer je een lecture toevoegt of aanpast.
 */

package lectureinfodialog;

import database.DataTransferObjects.*;
import guielements.LectureRepresentation;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;


public class LectureInputController {
    private LectureInput lectureInput;
    public ChoiceBox<SimpleDTO> studentGroupChoiceBox;
    public ChoiceBox<SimpleDTO> teacherChoiceBox;
    public ChoiceBox<SimpleDTO> locationChoiceBox;
    public TextField courseNameTextField;
    public ChoiceBox<Day> dayChoiceBox;
    public ChoiceBox<Period> periodChoiceBox;
    public ChoiceBox<Integer> durationChoiceBox;

    private List<ChoiceBox> choiceBoxes;

    public void initialize(){
        choiceBoxes = new ArrayList<>();
        choiceBoxes.add(studentGroupChoiceBox);
        choiceBoxes.add(teacherChoiceBox);
        choiceBoxes.add(locationChoiceBox);
        choiceBoxes.add(dayChoiceBox);
        choiceBoxes.add(periodChoiceBox);
        choiceBoxes.add(durationChoiceBox);
    }

    /*
    Vult eerst de 3 simpleDTO comboboxes op en maakt daarna een lijst van objecten om in de andere boxes te steken.
     */
    public void initializeChoiceBoxes(List<SimpleDTO> studentgroups, List<SimpleDTO> teachers, List<SimpleDTO> locations, List<String> startHours){
        studentGroupChoiceBox.getItems().addAll(studentgroups);
        teacherChoiceBox.getItems().addAll(teachers);
        locationChoiceBox.getItems().addAll(locations);

        String[] dayNames = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        List<Period> periods = new ArrayList<>();
        List<Day> days = new ArrayList<>();
        int teller = 1;
        for (String startHour : startHours){
            periods.add(new Period(teller, startHour));
            if (teller <= 5) {
                days.add(new Day(teller, dayNames[teller - 1]));
            }
            teller += 1;
        }
        dayChoiceBox.getItems().addAll(days);
        periodChoiceBox.getItems().addAll(periods);

        periodChoiceBox.getSelectionModel().selectedItemProperty().addListener(e -> fillDurationChoiceBox(periodChoiceBox.getSelectionModel().getSelectedItem().getId(), startHours.size()));
    }

    private void fillDurationChoiceBox(int selectedPeriodNr, int maxPeriodNr){
        durationChoiceBox.getItems().clear();

        for (int i = 1; i <= maxPeriodNr-selectedPeriodNr+1; i++){
            durationChoiceBox.getItems().add(i);
        }
        durationChoiceBox.setValue(1);
    }

    public void save(){
        boolean wasCorrect = true;
        //overloop inputvelden, als een selectie ongeldig is, geef aan op GUI
        for (ChoiceBox choiceBox : choiceBoxes){
            if (choiceBox.getSelectionModel().getSelectedItem() == null){
                choiceBox.getStyleClass().add("incorrectinput");
                wasCorrect = false;
            } else {
                choiceBox.getStyleClass().removeAll("incorrectinput");
            }
        }
        String courseName = courseNameTextField.getText();
        if (courseName.isEmpty() || courseName.contains("\"")){
            courseNameTextField.getStyleClass().add("incorrectinput");
            wasCorrect = false;
        } else {
            courseNameTextField.getStyleClass().removeAll("incorrectinput");
        }
        if (! wasCorrect){
            return;
        }


        //check of coursenaam geldig is

        LectureDTO lectureDTO = new LectureDTO(studentGroupChoiceBox.getSelectionModel().getSelectedItem().getId(),
                teacherChoiceBox.getSelectionModel().getSelectedItem().getId(),
                locationChoiceBox.getSelectionModel().getSelectedItem().getId(),
                courseNameTextField.getText(),
                dayChoiceBox.getSelectionModel().getSelectedItem().getId(),
                periodChoiceBox.getSelectionModel().getSelectedItem().getId(),
                durationChoiceBox.getSelectionModel().getSelectedItem());

        lectureInput.setLectureDTO(lectureDTO);
        lectureInput.close();
    }

    public void cancel(){
        lectureInput.close();
    }

    public LectureInput getLectureInput() {
        return lectureInput;
    }

    public void setLectureInput(LectureInput lectureInput) {
        this.lectureInput = lectureInput;
    }

    public void setStartData(LectureRepresentation selectedLecture){

    }
}

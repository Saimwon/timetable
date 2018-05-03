/*
Simon Van Braeckel
Controller voor het venster dat zich opent wanneer je een lecture toevoegt of aanpast.
 */

package lectureinfodialog;

import datatransferobjects.DayDTO;
import datatransferobjects.LectureDTO;
import datatransferobjects.PeriodDTO;
import datatransferobjects.SimpleDTO;
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
    public ChoiceBox<DayDTO> dayChoiceBox;
    public ChoiceBox<PeriodDTO> periodChoiceBox;
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


    public void fillChoiceBoxes(List<SimpleDTO> studentgroups, List<SimpleDTO> teachers, List<SimpleDTO> locations, List<String> startHours){
        String[] dayNames = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        List<PeriodDTO> periods = new ArrayList<>();
        List<DayDTO> days = new ArrayList<>();
        int teller = 1;
        for (String startHour : startHours) {
            periods.add(new PeriodDTO(teller, startHour));
            if (teller <= 5) {
                days.add(new DayDTO(teller, dayNames[teller - 1]));
            }
            teller += 1;
        }

        periodChoiceBox.getSelectionModel().selectedItemProperty().addListener(e ->
                fillDurationChoiceBox(periodChoiceBox.getSelectionModel().getSelectedIndex(), startHours.size()-1));
        fillAndSelectFirst(studentGroupChoiceBox, studentgroups);
        fillAndSelectFirst(teacherChoiceBox, teachers);
        fillAndSelectFirst(locationChoiceBox, locations);
        fillAndSelectFirst(dayChoiceBox, days);
        fillAndSelectFirst(periodChoiceBox, periods);
    }

    protected <T> void fillAndSelectFirst(ChoiceBox<T> choiceBox, List<T> content){
        choiceBox.getItems().addAll(content);
        choiceBox.getSelectionModel().selectFirst();
    }

    private void fillDurationChoiceBox(int selectedPeriodNr, int maxPeriodNr){
        durationChoiceBox.getItems().clear();

        for (int i = 1; i <= maxPeriodNr-selectedPeriodNr+1; i++){
            durationChoiceBox.getItems().add(i);
        }
        durationChoiceBox.getSelectionModel().selectFirst();
    }

    public void save(){
        boolean wasCorrect = checkInputs();
        if (! wasCorrect){
            return;
        }

        lectureInput.setResultLectureDTO(makeLectureDTO());
        lectureInput.close();
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public LectureDTO makeLectureDTO(){
        LectureDTO lectureDTO = new LectureDTO(studentGroupChoiceBox.getSelectionModel().getSelectedItem().getId(),
                teacherChoiceBox.getSelectionModel().getSelectedItem().getId(),
                locationChoiceBox.getSelectionModel().getSelectedItem().getId(),
                courseNameTextField.getText(),
                dayChoiceBox.getSelectionModel().getSelectedItem().getId(),
                periodChoiceBox.getSelectionModel().getSelectedItem().getId(),
                durationChoiceBox.getSelectionModel().getSelectedItem());

        return lectureDTO;
    }

    protected boolean checkInputs(){
        boolean wasCorrect = true;
        //overloop inputvelden, als een selectie ongeldig is, geef aan in GUI
        for (ChoiceBox choiceBox : choiceBoxes){
            if (choiceBox.getSelectionModel().getSelectedItem() == null){
                choiceBox.getStyleClass().add("incorrectinput");
                wasCorrect = false;
            } else {
                choiceBox.getStyleClass().removeAll("incorrectinput");
            }
        }
        if (! checkTextField()){
            wasCorrect = false;
        }
        return wasCorrect;
    }

    protected boolean checkTextField(){
        boolean wasCorrect = true;
        String courseName = courseNameTextField.getText();
        if (courseName.isEmpty() || courseName.contains("\"")){
            courseNameTextField.getStyleClass().add("incorrectinput");
            wasCorrect = false;
        } else {
            courseNameTextField.getStyleClass().removeAll("incorrectinput");
        }
        return wasCorrect;
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
}

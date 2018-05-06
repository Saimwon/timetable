/*
Simon Van Braeckel
Controller voor het venster dat zich opent wanneer je een lecture toevoegt of aanpast.
 */

package lectureinfodialog;

import datatransferobjects.*;
import guielements.ErrorDialog;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import timetable.ListViewModel;

import java.util.ArrayList;
import java.util.List;


public class LectureInputController {
    private LectureInput lectureInput;
    private ListViewModel listViewModel;

    public ChoiceBox<StudentGroupDTO> studentGroupChoiceBox;
    public ChoiceBox<TeacherDTO> teacherChoiceBox;
    public ChoiceBox<LocationDTO> locationChoiceBox;
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

    /*
    Bind inhoud van Choiceboxes aan juiste listview uit listviewmodel. Zorgt ervoor dat alles vanzelf update
    als we iets toevoegen eraan
     */
    private void bindChoiceBoxes(ListViewModel listViewModel){
        studentGroupChoiceBox.itemsProperty().bind(listViewModel.studentGroupDTOListProperty());
        studentGroupChoiceBox.getSelectionModel().selectFirst();
        teacherChoiceBox.itemsProperty().bind(listViewModel.teacherDTOListProperty());
        teacherChoiceBox.getSelectionModel().selectFirst();
        locationChoiceBox.itemsProperty().bind(listViewModel.locationDTOListProperty());
        locationChoiceBox.getSelectionModel().selectFirst();
    }

    /*
    Vul choiceboxes op met inhoud uit databank en modellen.
     */
    public void fillChoiceBoxes(List<String> startHours){
        fillDayAndPeriodChoiceBoxes(startHours);

        //Luisteraar die durationchoiceboxopties zal aanpassen wanneer er een period geselecteerd wordt.
        periodChoiceBox.getSelectionModel().selectedItemProperty().addListener(e ->
                fillDurationChoiceBox(periodChoiceBox.getSelectionModel().getSelectedIndex(), startHours.size()-1));

        bindChoiceBoxes(this.listViewModel);
    }

    /*
    Maak nieuwe Day en Period DTO's om in de day en period choiceboxes te steken, en steek ze erin.
     */
    private void fillDayAndPeriodChoiceBoxes(List<String> startHours){
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

        fill(dayChoiceBox, days);
        fill(periodChoiceBox, periods);
    }

    /*
    Vult choicebox op met meegegeven inhoud en selecteert eerst optie.
     */
    protected <T> void fill(ChoiceBox<T> choiceBox, List<T> content){
        choiceBox.getItems().addAll(content);
        choiceBox.getSelectionModel().selectFirst();
    }

    /*
    Vul durationchoicebox met Integers afhankelijk van hoeveel periods er nog over zijn vooreen lecture die begint
    op de period op plaats "selectedPeriodNr"
     */
    private void fillDurationChoiceBox(int selectedPeriodNr, int maxPeriodNr){
        durationChoiceBox.getItems().clear();

        for (int i = 1; i <= maxPeriodNr-selectedPeriodNr+1; i++){
            durationChoiceBox.getItems().add(i);
        }
        durationChoiceBox.getSelectionModel().selectFirst();
    }

    /*
    Kijk of inputs die gegeven zijn door de gebruiker goed zijn.
    Zo ja, maak de terug te geven lectureDTO, geef die aan de bijhorende
    LectureInput klasse en sluit dit venster.
     */
    public void save(){
        boolean wasCorrect = checkInputs();
        if (! wasCorrect){
            return;
        }

        lectureInput.setResultLectureDTO(makeLectureDTO());
        lectureInput.close();
    }

    /*
    Maak de terug te geven lectureDTO op adhv inputs.
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    private LectureDTO makeLectureDTO(){
        LectureDTO lectureDTO = new LectureDTO(studentGroupChoiceBox.getSelectionModel().getSelectedItem().getId(),
                teacherChoiceBox.getSelectionModel().getSelectedItem().getId(),
                locationChoiceBox.getSelectionModel().getSelectedItem().getId(),
                courseNameTextField.getText(),
                dayChoiceBox.getSelectionModel().getSelectedItem().getId(),
                periodChoiceBox.getSelectionModel().getSelectedItem().getId(),
                durationChoiceBox.getSelectionModel().getSelectedItem());

        return lectureDTO;
    }

    /*
    overloop alle inputvelden en geef aan wanneer iets niet ingevuld is.
     */
    protected boolean checkInputs(){
        boolean wasCorrect = true;

        //overloop inputvelden, als een selectie null is, geef aan in GUI
        for (ChoiceBox choiceBox : choiceBoxes){
            if (choiceBox.getSelectionModel().getSelectedItem() == null){
                choiceBox.getStyleClass().add("incorrectinput");
                wasCorrect = false;
            } else {
                choiceBox.getStyleClass().removeAll("incorrectinput");
            }
        }
        if (! checkTextField(courseNameTextField)){
            wasCorrect = false;
        }

        return wasCorrect;
    }

    /*
    Geef aan wanneer de input uit het textveld ongeldig is.
     */
    protected boolean checkTextField(TextField textField){
        String courseName = textField.getText();
        if (courseName.isEmpty()){
            textField.getStyleClass().add("incorrectinput");
            return false;
        } else {
            textField.getStyleClass().removeAll("incorrectinput");
            return true;
        }
    }

    /*
    Tegenhanger van de save-knop. Sluit dit venster en laat resultveld in LectureInput op null.
     */
    public void cancel(){
        lectureInput.close();
    }

    public LectureInput getLectureInput() {
        return lectureInput;
    }

    public void setLectureInput(LectureInput lectureInput) {
        this.lectureInput = lectureInput;
    }

    public void setListViewModel(ListViewModel listViewModel) {
        this.listViewModel = listViewModel;
    }
}

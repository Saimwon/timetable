/*
Simon Van Braeckel
 */

package lectureinfodialog;

import datatransferobjects.*;
import guielements.LectureRepresentation;
import javafx.scene.control.ChoiceBox;
import timetable.ListViewModel;

import java.util.List;

public class EditLectureInputController extends LectureInputController {
    private LectureRepresentation selectedLecture;

    public EditLectureInputController(LectureRepresentation selectedLecture){
        this.selectedLecture = selectedLecture;
    }

    @Override
    public boolean checkInputs(){
        return checkTextField(courseNameTextField);
    }

    @Override
    protected <T> void fill(ChoiceBox<T> choiceBox, List<T> content) {
        choiceBox.getItems().setAll(content);
    }

    /*
    Selecteer data uit selectedlecture in de choiceboxes. Zo is het duidelijk wat er gekozen wordt als je
    een selectie laat hoe hij staat.
     */
    private void selectChoiceBoxData(){
        LectureDTO lectureDTO = selectedLecture.getLectureDTO();

        selecteerJuiste(studentGroupChoiceBox, lectureDTO.getStudent_id());
        selecteerJuiste(teacherChoiceBox, lectureDTO.getTeacher_id());
        selecteerJuiste(locationChoiceBox, lectureDTO.getLocation_id());
        selecteerJuiste(dayChoiceBox, lectureDTO.getDay());
        selecteerJuiste(periodChoiceBox, lectureDTO.getFirst_block());
        durationChoiceBox.getSelectionModel().select(lectureDTO.getDuration()-1);
        courseNameTextField.setText(lectureDTO.getCourse());
    }

    /*
    Zoek DTO met gezochte ID in choicebox en selecteer die.
     */
    private <T extends NameIdDTO> void selecteerJuiste(ChoiceBox<T> choiceBox, int id){
        int i = 0;
        while (i < choiceBox.getItems().size() && choiceBox.getItems().get(i).getId() != id){
            i++;
        }
        choiceBox.getSelectionModel().select(i);
    }

    @Override
    public void fillChoiceBoxes(List<String> startHours){
        super.fillChoiceBoxes(startHours);
        selectChoiceBoxData();
    }
}
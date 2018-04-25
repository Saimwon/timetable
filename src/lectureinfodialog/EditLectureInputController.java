/*
Simon Van Braeckel
 */

package lectureinfodialog;

import datatransferobjects.LectureDTO;
import guielements.LectureRepresentation;
import javafx.scene.control.ChoiceBox;

import java.util.List;

public class EditLectureInputController extends LectureInputController{
    private LectureRepresentation selectedLecture;

    public EditLectureInputController(LectureRepresentation selectedLecture){
        this.selectedLecture = selectedLecture;
    }

    @Override
    public boolean checkInputs(){
        return checkTextField();
    }

    @Override
    protected boolean checkTextField(){
        String courseName = courseNameTextField.getText();
        if (courseName.contains("\"")){
            courseNameTextField.getStyleClass().add("incorrectinput");
            return false;
        } else {
            courseNameTextField.getStyleClass().removeAll("incorrectinput");
            return true;
        }
    }

    @Override
    protected void fillAndSelectFirst(ChoiceBox choiceBox, List content){
        choiceBox.getItems().addAll(content);
    }

    @Override
    public LectureDTO makeLectureDTO(){
        LectureDTO lectureDTO = new LectureDTO(studentGroupChoiceBox.getSelectionModel().getSelectedItem() == null ? selectedLecture.getLectureDTO().getStudent_id() : studentGroupChoiceBox.getSelectionModel().getSelectedItem().getId(),
                teacherChoiceBox.getSelectionModel().getSelectedItem() == null ? selectedLecture.getLectureDTO().getTeacher_id() : teacherChoiceBox.getSelectionModel().getSelectedItem().getId(),
                locationChoiceBox.getSelectionModel().getSelectedItem() == null ? selectedLecture.getLectureDTO().getLocation_id() : locationChoiceBox.getSelectionModel().getSelectedItem().getId(),
                courseNameTextField.getText().equals("") ? selectedLecture.getCourseName() : courseNameTextField.getText(),
                dayChoiceBox.getSelectionModel().getSelectedItem() == null ? selectedLecture.getLectureDTO().getDay() : dayChoiceBox.getSelectionModel().getSelectedItem().getId(),
                periodChoiceBox.getSelectionModel().getSelectedItem() == null ? selectedLecture.getLectureDTO().getFirst_block() : periodChoiceBox.getSelectionModel().getSelectedItem().getId(),
                durationChoiceBox.getSelectionModel().getSelectedItem() == null ? selectedLecture.getLectureDTO().getDuration() : durationChoiceBox.getSelectionModel().getSelectedItem());

        return lectureDTO;
    }
}
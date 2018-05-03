/*
Simon Van Braeckel
 */

package lectureinfodialog;

import datatransferobjects.LectureDTO;
import datatransferobjects.NameIdDTO;
import datatransferobjects.SimpleDTO;
import guielements.LectureRepresentation;
import javafx.scene.control.ChoiceBox;

import java.util.List;

public class EditLectureInputController extends LectureInputController {
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
    protected <T> void fillAndSelectFirst(ChoiceBox<T> choiceBox, List<T> content) {
        choiceBox.getItems().setAll(content);
    }

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

    private <T extends NameIdDTO> void selecteerJuiste(ChoiceBox<T> choiceBox, int id){
        int i = 0;
        while (i < choiceBox.getItems().size() && choiceBox.getItems().get(i).getId() != id){
            i++;
        }
        choiceBox.getSelectionModel().select(i);
    }

    @Override
    public LectureDTO makeLectureDTO(){
        int student_id = studentGroupChoiceBox.getSelectionModel().getSelectedItem() == null ?
                selectedLecture.getLectureDTO().getStudent_id() :
                studentGroupChoiceBox.getSelectionModel().getSelectedItem().getId();
        int teacher_id = teacherChoiceBox.getSelectionModel().getSelectedItem() == null ?
                selectedLecture.getLectureDTO().getTeacher_id() :
                teacherChoiceBox.getSelectionModel().getSelectedItem().getId();
        int location_id = locationChoiceBox.getSelectionModel().getSelectedItem() == null ?
                selectedLecture.getLectureDTO().getLocation_id() :
                locationChoiceBox.getSelectionModel().getSelectedItem().getId();
        String courseName = courseNameTextField.getText().equals("") ?
                selectedLecture.getCourseName() :
                courseNameTextField.getText();
        int day = dayChoiceBox.getSelectionModel().getSelectedItem() == null ?
                selectedLecture.getLectureDTO().getDay() :
                dayChoiceBox.getSelectionModel().getSelectedItem().getId();
        int period = periodChoiceBox.getSelectionModel().getSelectedItem() == null ?
                selectedLecture.getLectureDTO().getFirst_block() :
                periodChoiceBox.getSelectionModel().getSelectedItem().getId();
        int duration = durationChoiceBox.getSelectionModel().getSelectedItem() == null ?
                selectedLecture.getLectureDTO().getDuration() :
                durationChoiceBox.getSelectionModel().getSelectedItem();

        return new LectureDTO(student_id, teacher_id, location_id, courseName, day, period, duration);
    }

    @Override
    public void fillChoiceBoxes(List<SimpleDTO> studentgroups, List<SimpleDTO> teachers, List<SimpleDTO> locations,
                                List<String> startHours){
        super.fillChoiceBoxes(studentgroups, teachers, locations, startHours);
        selectChoiceBoxData();
    }
}
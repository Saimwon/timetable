/*
Simon Van Braeckel
 */

package lectureinfodialog;

import datatransferobjects.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LectureInput extends Stage {
    private LectureDTO resultLectureDTO;

    public LectureInput(List<StudentGroupDTO> studentgroups, List<TeacherDTO> teachers, List<LocationDTO> locations, List<String> startHours, LectureInputController controller){
        resultLectureDTO = null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("lectureinput.fxml"));
        loader.setController(controller);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e){
            //Crash als het programma de fxml file van deze klasse niet vindt of als er een fout in staat.
            e.printStackTrace();
            System.exit(1);
        }
        this.setTitle("New Lecture");

        Scene scene = new Scene(root);
        this.setScene(scene);

        controller.setLectureInput(this);
        controller.fillChoiceBoxes(studentgroups, teachers, locations, startHours);
    }

    public LectureDTO getResultLectureDTO() {
        return resultLectureDTO;
    }

    public void setResultLectureDTO(LectureDTO resultLectureDTO) {
        this.resultLectureDTO = resultLectureDTO;
    }
}

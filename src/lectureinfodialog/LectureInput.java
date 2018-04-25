/*
Simon Van Braeckel
 */

package lectureinfodialog;

import datatransferobjects.LectureDTO;
import datatransferobjects.SimpleDTO;
import guielements.LectureRepresentation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LectureInput extends Stage{
    private LectureDTO lectureDTO;
    private LectureInputController controller;

    public LectureInput(List<SimpleDTO> studentgroups, List<SimpleDTO> teachers, List<SimpleDTO> locations, List<String> startHours, LectureRepresentation lectureRepresentation){
        lectureDTO = null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("lectureinput.fxml"));
        Parent root = null;
        try {
            if (lectureRepresentation == null) {
                loader.setController(new LectureInputController());
            } else {
                loader.setController(new EditLectureInputController(lectureRepresentation));
            }
            root = loader.load();
        } catch (IOException e){
            //Crash als het programma de fxml file van deze klasse niet vindt of als er een fout in staat.
            e.printStackTrace();
            System.exit(1);
        }
        this.setTitle("New Lecture");

        Scene scene = new Scene(root);
        this.setScene(scene);

        controller = loader.getController();
        controller.setLectureInput(this);
        controller.fillChoiceBoxes(studentgroups, teachers, locations, startHours);
    }

    public LectureDTO getLectureDTO() {
        return lectureDTO;
    }

    public void setLectureDTO(LectureDTO lectureDTO) {
        this.lectureDTO = lectureDTO;
    }
}

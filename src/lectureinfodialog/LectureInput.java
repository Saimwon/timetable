/*
Simon Van Braeckel
 */

package lectureinfodialog;

import database.DataTransferObjects.*;
import guielements.LectureRepresentation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LectureInput extends Stage{
    private LectureDTO lectureDTO;
    LectureInputController controller;

    public LectureInput(List<SimpleDTO> studentgroups, List<SimpleDTO> teachers, List<SimpleDTO> locations, List<String> startHours){
        lectureDTO = null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("lectureinput.fxml"));
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

        controller = loader.getController();
        controller.setLectureInput(this);
        controller.initializeChoiceBoxes(studentgroups, teachers, locations, startHours);
    }

    public LectureInput(List<SimpleDTO> studentgroups, List<SimpleDTO> teachers, List<SimpleDTO> locations, List<String> startHours, LectureRepresentation selectedLecture){
        this(studentgroups, teachers, locations, startHours);
        controller.setStartData(selectedLecture);
    }

    public LectureDTO getLectureDTO() {
        return lectureDTO;
    }

    public void setLectureDTO(LectureDTO lectureDTO) {
        this.lectureDTO = lectureDTO;
    }
}

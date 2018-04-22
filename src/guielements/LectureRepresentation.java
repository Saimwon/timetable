/*
Van Braeckel Simon
 */

package guielements;

import database.DataTransferObjects.LectureDTO;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LectureRepresentation extends VBox {
    private String teacherName;
    private String courseName;
    private List<String> infoList;
    private LectureDTO lectureDTO;

    public LectureRepresentation(String name1, String profName1, LectureDTO lectureDTO){
        this.lectureDTO = lectureDTO;
        this.getStyleClass().add("lecture");
        this.teacherName = profName1;
        this.courseName = name1;
        initialize();
    }

    public String getCourseName(){
        return courseName;
    }
    public String getTeacherName(){
        return teacherName;
    }

    private void initialize(){
        getChildren().add(new Label(courseName));
        getChildren().add(new Label(teacherName));
    }

    public LectureDTO getLectureDTO() {
        return lectureDTO;
    }

    public void setLectureDTO(LectureDTO lec) {
        this.lectureDTO = lec;
    }
}
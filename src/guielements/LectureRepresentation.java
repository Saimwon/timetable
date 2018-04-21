/*
Van Braeckel Simon
 */

package guielements;

import database.DataTransferObjects.LectureDTO;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class LectureRepresentation extends VBox {
    private List<String> infoList;
    private LectureDTO lectureDTO;

    public LectureRepresentation(String name1, String profName1, LectureDTO lectureDTO){
        this.lectureDTO = lectureDTO;
        this.getStyleClass().add("lecture");
        //this.setAlignment(Pos.CENTER);
        this.
        infoList = new ArrayList<>();
        infoList.add(name1);
        infoList.add(profName1);
        initialize();
    }

    public String getCourse(){
        return infoList.get(0);
    }

    private void initialize(){
        for (String info : infoList){
            Label lab =  new Label(info);
            getChildren().add(lab);
        }
    }

    public LectureDTO getLectureDTO() {
        return lectureDTO;
    }


    public void setLectureDTO(LectureDTO lec) {
        this.lectureDTO = lec;
    }
}
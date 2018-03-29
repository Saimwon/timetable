package guielements;

import database.DTO.LectureDTO;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class LesuurContainer extends HBox {
    private List<LectureRepresentation> lessen;

    public LesuurContainer(){
        lessen = new ArrayList<>();
    }

    public void test(){
        getChildren().add(new Label("simonvanbraeckel"));
    }
}

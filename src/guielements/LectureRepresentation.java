/*
Van Braeckel Simon
 */

package guielements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class LectureRepresentation extends VBox {
    private List<String> infoList;

    public LectureRepresentation(String name1, String profName1){
        this.getStyleClass().add("lecture");
        infoList = new ArrayList<>();
        infoList.add(name1);
        infoList.add(profName1);
        initialize();
    }

    private void initialize(){
        for (String info : infoList){
            Label lab =  new Label(info);
            getChildren().add(lab);
        }
    }
}
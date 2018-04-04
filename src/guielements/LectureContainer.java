/*
Van Braeckel Simon
 */

package guielements;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LectureContainer extends VBox {
    private ScrollPane scrollPane; //Dit is een veld zodat je gemakkelijk op het eerste zicht ziet dat dit component ook een scrollpane bevat
    private VBox content;

    public LectureContainer(){
        this.setFillWidth(true);

        this.scrollPane = new ScrollPane();
        this.content = new VBox();

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHvalue(0.5);
        scrollPane.setContent(content);

        this.getChildren().add(scrollPane);
        this.getStyleClass().add("lecture");
        this.getStyleClass().add("correctlecture");
    }

    public void notifyOfChange(){
        int amounfOfChildren = content.getChildren().size();
        if (amounfOfChildren > 1){
            this.getStyleClass().clear();
            this.getStyleClass().add("incorrectlecture");
        }
        if (amounfOfChildren <= 1){
            this.getStyleClass().clear();
            this.getStyleClass().add("correctlecture");
        }
    }

    public void addLecture(LectureRepresentation lecture){
        content.getChildren().add(lecture);
    }
}

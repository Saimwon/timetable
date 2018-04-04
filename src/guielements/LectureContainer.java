/*
Van Braeckel Simon
 */

package guielements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class LectureContainer extends VBox {
    private ScrollPane scrollPane; //Dit is een veld zodat je gemakkelijk op het eerste zicht ziet dat dit component ook een scrollpane bevat
    private VBox contentBox;

    public LectureContainer(){
        this.setFillWidth(true);

        this.contentBox = new VBox();
        contentBox.setAlignment(Pos.CENTER);


        this.scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHvalue(0.5);
        scrollPane.setContent(contentBox);

        this.getChildren().add(scrollPane);
        this.getStyleClass().add("lecture");
        this.getStyleClass().add("correctlecture");
    }

    public void notifyOfChange(){
        int amounfOfChildren = contentBox.getChildren().size();
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
        contentBox.getChildren().add(lecture);
    }
}

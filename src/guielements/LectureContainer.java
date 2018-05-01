/*
Van Braeckel Simon
 */

package guielements;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class LectureContainer extends VBox {
    private VBox contentBox;

    public LectureContainer(){
        this.setFillWidth(true);

        this.contentBox = new VBox();
        contentBox.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
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
            this.getStyleClass().removeAll("correctlecture");
            this.getStyleClass().add("incorrectlecture");
        } else if (amounfOfChildren == 1){
            this.getStyleClass().removeAll("incorrectlecture");
            this.getStyleClass().add("correctlecture");
        }
    }

    public void addLecture(LectureRepresentation lecture){
        contentBox.getChildren().add(lecture);
    }
}

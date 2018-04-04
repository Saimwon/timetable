/*
Van Braeckel Simon
 */

package guielements;


import database.interfaces.implementations.SQLiteDataAccessProvider;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

import java.util.List;

public class MyTable extends GridPane {

    public MyTable(){
    }


    public void initializeRows(List<String> startUren){
        // Rijen invoegen met juiste hoogte
        int aantalRijen = startUren.size();
        for (int i = 0; i < aantalRijen; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(95.0/aantalRijen);
            row.setMaxHeight(95.0/aantalRijen);
            this.getRowConstraints().add(row);
        }
        // Juiste uren invullen
        int teller = 1;
        for (String startUur : startUren){
            HBox uurHBox = new HBox();
            uurHBox.setAlignment(Pos.CENTER);
            uurHBox.getChildren().add(new Label(startUur));
            this.addRow(teller, uurHBox);
            this.getRowConstraints().add(new RowConstraints());
            teller++;
        }
    }

}

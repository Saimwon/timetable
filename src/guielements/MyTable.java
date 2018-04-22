/*
Van Braeckel Simon
 */

package guielements;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;

public class MyTable extends GridPane {
    public List<HBox> hboxLijst;

    public MyTable(){
        hboxLijst = new ArrayList<>();
    }


    public void initializeStartHours(List<String> startUren){
        //maak eerste kolom leeg
        this.getChildren().removeAll(hboxLijst);
        //maak leeg
        this.getRowConstraints().clear();
        //voeg eerst toe om de eerste rij klein te maken
        RowConstraints firstRow = new RowConstraints();
        firstRow.setPercentHeight(5);
        this.getRowConstraints().add(firstRow);

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
            hboxLijst.add(uurHBox);
            uurHBox.setAlignment(Pos.CENTER);
            uurHBox.getChildren().add(new Label(startUur));
            //this.addRow(teller, uurHBox);
            GridPane.setConstraints(uurHBox, 0, teller);
            this.getChildren().add(uurHBox);
            this.getRowConstraints().add(new RowConstraints());
            teller++;
        }
    }

}

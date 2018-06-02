/*
Simon Van Braeckel
 */

package timetable;

import guielements.LectureContainer;
import guielements.LectureRepresentation;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;

public class TimetableView extends GridPane implements InvalidationListener {
    private List<HBox> hboxLijst;
    private TimetableModel timetableModel;
    private MainWindowController mainWindowController;

    public TimetableView(){
        hboxLijst = new ArrayList<>();
        this.timetableModel = null;
        this.mainWindowController = null;
    }

    /**
     * Vul de eerste kolom waar de starturen in staan in.
     * @param startUren
     */
    private void initializeStartHours(List<String> startUren){
        //maak eerste kolom leeg
        this.getChildren().removeAll(hboxLijst);
        hboxLijst.clear();
        //verwijder rowconstraints
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

    public void setModel(TimetableModel timetableModel){
        this.timetableModel = timetableModel;
        timetableModel.addListener(this);
    }

    /**
     * Wordt opgeroepen door het model wanneer er veranderd wordt van databank.
     * @param observable
     */
    @Override
    public void invalidated(Observable observable) {
        List<String> startHours = timetableModel.getStartHours();

        initializeStartHours(startHours);

        List<List<ObservableList<LectureRepresentation>>> table = timetableModel.getTable();

        for (int i = 0; i < table.size(); i++){
            List<ObservableList<LectureRepresentation>> kolom = table.get(i);
            for (int j = 0; j < kolom.size(); j++){
                LectureContainer lectureContainer = new LectureContainer();
                lectureContainer.setModel(kolom.get(j));
                lectureContainer.setRij(j+1);
                lectureContainer.setMainWindowController(mainWindowController);
                lectureContainer.setKolom(i+1);

                GridPane.setConstraints(lectureContainer, i+1, j+1);
                this.getChildren().add(lectureContainer);
            }
        }
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
}
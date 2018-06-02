/*
Van Braeckel Simon
 */

package guielements;

import datatransferobjects.LectureDTO;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import timetable.MainWindowController;

public class LectureContainer extends VBox implements InvalidationListener {
    private VBox contentBox;
    private MainWindowController mainWindowController;

    private int rij;
    private int kolom;

    private ObservableList<LectureRepresentation> lectureList; //veld voor corresponderende cel in Model

    public LectureContainer() {
        this.setFillWidth(true);

        this.contentBox = new VBox();
        contentBox.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHvalue(0.5);
        scrollPane.setContent(contentBox);

        this.setOnDragOver(this::onDragOver);
        this.setOnDragDropped(this::onDragDropped);

        this.getChildren().add(scrollPane);

        this.mainWindowController = mainWindowController;
    }

    private void updateStyleClass(){
        int amounfOfChildren = contentBox.getChildren().size();
        if (amounfOfChildren > 0){
            addAndRemove("filledlecturecontainer", "filledlecturecontainer");
        }
        if (amounfOfChildren > 1){
            addAndRemove("incorrectlecture", "correctlecture");
        } else if (amounfOfChildren == 1){
            addAndRemove("correctlecture", "incorrectlecture");
        } else {
            this.getStyleClass().removeAll("incorrectlecture", "correctlecture", "filledlecturecontainer");
        }
    }

    private void addAndRemove(String toAdd, String toRemove){
        this.getStyleClass().removeAll(toRemove);
        this.getStyleClass().add(toAdd);
    }

    @Override
    public void invalidated(Observable observable) {
        contentBox.getChildren().clear();

        for (LectureRepresentation lectureRepresentation : lectureList){
            contentBox.getChildren().add(lectureRepresentation);
        }

        updateStyleClass();
    }

    public void setModel(ObservableList<LectureRepresentation> lectureList){
        this.lectureList = lectureList;
        lectureList.addListener(this);
    }

    public void setMainWindowController(MainWindowController mainWindowController){
        this.mainWindowController = mainWindowController;
    }

    public int getRij() {
        return rij;
    }

    public void setRij(int rij) {
        this.rij = rij;
    }

    public int getKolom() {
        return kolom;
    }

    public void setKolom(int kolom) {
        this.kolom = kolom;
    }



    //Drag and Drop
    public void onDragOver(DragEvent ev){
        if (ev.getDragboard().hasContent(mainWindowController.CUSTOM_LECTUREDTO) && ! this.lectureList.contains(ev.getGestureSource())){
            ev.acceptTransferModes(TransferMode.MOVE);
        }

        ev.consume();
    }

    public void onDragDropped(DragEvent ev){
        if (ev.getDragboard().hasContent(mainWindowController.CUSTOM_LECTUREDTO)) {
            LectureDTO lectureDTO = (LectureDTO) ev.getDragboard().getContent(mainWindowController.CUSTOM_LECTUREDTO);
            LectureDTO newlectureDTO = new LectureDTO(lectureDTO.getStudent_id(), lectureDTO.getTeacher_id(),
                    lectureDTO.getLocation_id(), lectureDTO.getCourse(), kolom, rij, Math.min(lectureDTO.getDuration(), mainWindowController.getTimetableModel().getStartHours().size()- (rij)+1));

            mainWindowController.commitLectureEdit(lectureDTO, newlectureDTO);

            ev.setDropCompleted(true);
        } else {
            ev.setDropCompleted(false);
        }

        ev.consume();
    }
}












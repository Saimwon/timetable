/*
Van Braeckel Simon
 */

package guielements;

import datatransferobjects.LectureDTO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import timetable.MainWindowController;

import java.util.ArrayList;
import java.util.List;

public class LectureRepresentation extends VBox {
    private List<LectureRepresentation> lectureGroup;

    private ContextMenu contextMenu;

    private String teacherName;
    private LectureDTO lectureDTO;
    private MainWindowController mainWindowController;

    public LectureRepresentation(String profName1, LectureDTO lectureDTO, MainWindowController mainWindowController){
        this.lectureGroup = new ArrayList<>();
        this.mainWindowController = mainWindowController;
        this.lectureDTO = lectureDTO;
        this.teacherName = profName1;
        this.setOnDragDetected(this::onDragDetected);

        this.getStyleClass().add("lecture");

        initialize();
        initializeContextMenu();

        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                mainWindowController.onLectureSelected(this);
                contextMenu.hide();
                contextMenu.show(this, event.getScreenX(), event.getScreenY());
            } else {
                mainWindowController.onLectureSelected(this);
                mainWindowController.editLecture(event.getScreenX(), event.getScreenY(), this);
            }
            ;
        });
    }

    private void initializeContextMenu(){
        contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Edit");
        item1.setOnAction(e -> mainWindowController.editLecture(contextMenu.getX(), contextMenu.getY()));
        MenuItem item2 = new MenuItem("Delete");
        item2.setOnAction(e -> mainWindowController.deleteLecture());
        contextMenu.getItems().addAll(item1, item2);
    }

    public void setLectureGroup(List<LectureRepresentation> lectureGroup){
        this.lectureGroup = lectureGroup;
    }

    public List<LectureRepresentation> getLectureGroup(){
        return this.lectureGroup;
    }

    public String getCourseName(){
        return this.lectureDTO.getCourse();
    }
    public String getTeacherName(){
        return teacherName;
    }

    private void initialize(){
        getChildren().add(new Label(this.getCourseName()));
        getChildren().add(new Label(teacherName));
    }

    public LectureDTO getLectureDTO() {
        return lectureDTO;
    }

    public void setLectureDTO(LectureDTO lec) {
        this.lectureDTO = lec;
    }

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }




    //Drag and Drop
    public void onDragDetected(MouseEvent ev){
        Dragboard db = startDragAndDrop(TransferMode.MOVE);

        ClipboardContent cc = new ClipboardContent();
        cc.put(mainWindowController.CUSTOM_LECTUREDTO, this.lectureDTO);
        db.setContent(cc);

        ev.consume();
    }
}








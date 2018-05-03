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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import timetable.MainWindowController;

import java.util.ArrayList;
import java.util.List;

public class LectureRepresentation extends VBox {
    private List<LectureRepresentation> lectureGroup;

    private ContextMenu contextMenu;

    private String teacherName;
    private String courseName;
    private LectureDTO lectureDTO;
    private MainWindowController mainWindowController;

    public LectureRepresentation(String name1, String profName1, LectureDTO lectureDTO,
                                 MainWindowController mainWindowController, List<LectureRepresentation> lectureGroup){
        this(name1, profName1, lectureDTO, mainWindowController);
        this.setLectureGroup(lectureGroup);
    }

    public LectureRepresentation(String name1, String profName1, LectureDTO lectureDTO, MainWindowController mainWindowController){
        this.lectureGroup = new ArrayList<>();

        this.mainWindowController = mainWindowController;
        this.lectureDTO = lectureDTO;
        this.getStyleClass().add("lecture");
        this.teacherName = profName1;
        this.courseName = name1;

        initialize();

        initializeContextMenu();
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() != MouseButton.SECONDARY) {
                mainWindowController.onLectureSelected(this);
                mainWindowController.editLecture(this);
            } else{
                mainWindowController.onLectureSelected(this);
                contextMenu.hide();
                contextMenu.show(this, event.getScreenX(), event.getScreenY());
            };
        });
    }

    private void initializeContextMenu(){
        contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Edit");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                mainWindowController.editLecture();
            }
        });
        MenuItem item2 = new MenuItem("Delete");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                mainWindowController.deleteLecture();
            }
        });
        contextMenu.getItems().addAll(item1, item2);
    }

    public void setLectureGroup(List<LectureRepresentation> lectureGroup){
        this.lectureGroup = lectureGroup;
    }

    public List<LectureRepresentation> getLectureGroup(){
        return this.lectureGroup;
    }

    public String getCourseName(){
        return courseName;
    }
    public String getTeacherName(){
        return teacherName;
    }

    private void initialize(){
        getChildren().add(new Label(courseName));
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
}
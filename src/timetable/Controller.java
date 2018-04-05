/*
Van Braeckel Simon
 */

package timetable;

import database.DTO.*;
import database.interfaces.LectureDAO;
import database.interfaces.implementations.SQLiteDataAccessProvider;
import guielements.LectureRepresentation;
import guielements.LectureContainer;
import guielements.MyTable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.*;

public class Controller {
    public MyTable gridPane;
    public ListView<StudentGroupDTO> studentGroupsView;
    public ListView<TeacherDTO> teachersView;
    public ListView<LocationDTO> locationsView;
    private Map<String, LectureContainer> containerMap;
    private Map<Integer, String> starthours;
    //veld dat geselecteeerde lecture bijhoudt
    private LectureRepresentation selected;

    public void initialize(){
        selected = null;
        gridPane.initializeRows(new SQLiteDataAccessProvider().getDataAccessContext().getPeriodDAO().getStartTimes());

        initializeViews();
        containerMap = new HashMap<>();
    }

    private void initializeViews(){
        List<TeacherDTO> teachers = new SQLiteDataAccessProvider().getDataAccessContext().getTeacherDAO().getTeachers();
        teachersView.getItems().setAll(teachers);
        teachersView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateTableContents("teacher_id", teachersView.getSelectionModel().getSelectedItem().getId()));

        List<StudentGroupDTO> studentGroups = new SQLiteDataAccessProvider().getDataAccessContext().getStudentDAO().getStudentGroups();
        studentGroupsView.getItems().setAll(studentGroups);
        studentGroupsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateTableContents("students_id", studentGroupsView.getSelectionModel().getSelectedItem().getId()));

        List<LocationDTO> locations = new SQLiteDataAccessProvider().getDataAccessContext().getLocationDAO().getLocations();
        locationsView.getItems().setAll(locations);
        locationsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateTableContents("location_id", locationsView.getSelectionModel().getSelectedItem().getId()));
    }

    public void updateTableContents(String columnName, int id){
        LectureDAO lectureDAO = new SQLiteDataAccessProvider().getDataAccessContext().getLectureDAO();
        List<LectureDTO> lectureList = lectureDAO.getLecturesFromColumnById(columnName, id);
        //clear gridpane containerobjecten
        gridPane.getChildren().removeAll(containerMap.values());
        //clear map
        containerMap = new HashMap<>();
        for (LectureDTO lec : lectureList){
            //loop over duration
            //als er nog geen entry is in de containermap voor die positie:
            //nieuwe lesuurcontainer maken en in map steken
            for (int i = 0; i < lec.getDuration(); i++){
                int row = lec.getDay();
                int column = lec.getFirst_block() + i;

                //key voor in map, we maken er een string van zodat hashen werkt.
                String positie = "" + row + column;

                LectureContainer container = new LectureContainer();
                if (!containerMap.containsKey(positie)) {
                    containerMap.put(positie, container);
                    //zet container in grid
                    GridPane.setConstraints(container, row, column);
                    gridPane.getChildren().add(container);
                } else {
                    container = containerMap.get(positie);
                }

                //zet lecture in container
                String teachname = new SQLiteDataAccessProvider().getDataAccessContext().getTeacherDAO().getTeacherByID(lec.getTeacher_id()).getName();
                LectureRepresentation lectureRepresentation = new LectureRepresentation(lec.getCourse(), teachname, lec);

                lectureRepresentation.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    System.out.println("klik gedetecteerd");
                    //verwijder
                    if (selected != null) {
                        selected.getStyleClass().remove("selectedlecture");
                    }
                    selected = (LectureRepresentation) event.getSource();
                    selected.getStyleClass().add("selectedlecture");
                });

                container.addLecture(lectureRepresentation);
                container.notifyOfChange();
            }
        }
    }
}

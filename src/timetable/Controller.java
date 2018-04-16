/*
Van Braeckel Simon
 */

package timetable;

import database.DTO.*;
import database.interfaces.*;
import database.interfaces.implementations.SQLiteDataAccessProvider;
import database.interfaces.SimpleDAO;
import guielements.LectureRepresentation;
import guielements.LectureContainer;
import guielements.MyTable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.*;

public class Controller {
    private DataAccessProvider dataAccessProvider;

    public TextField newEntryTextField;
    public Button newEntryButton;
    public Button renameEntryButton;
    public MyTable gridPane;
    public Accordion accordion;
    public ListView<SimpleDTO> studentGroupsView;
    public ListView<SimpleDTO> teachersView;
    public ListView<SimpleDTO> locationsView;
    private Map<String, LectureContainer> containerMap;
    private Map<Integer, String> starthours;
    //veld dat geselecteerde lecture bijhoudt:
    private LectureRepresentation selected;


    public void initialize() {
        dataAccessProvider = new SQLiteDataAccessProvider();
        selected = null;
        initializeGridPaneRows();

        initializeViews();
        containerMap = new HashMap<>();
    }

    public void initializeGridPaneRows(){
        gridPane.initializeStartHours(dataAccessProvider.getDataAccessContext().getPeriodDAO().getStartTimes());
    }

    public void initializeViews(){
        teachersView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> nameSelected("teacher_id", teachersView.getSelectionModel().getSelectedItem()));

        studentGroupsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> nameSelected("students_id", studentGroupsView.getSelectionModel().getSelectedItem()));

        locationsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> nameSelected("location_id", locationsView.getSelectionModel().getSelectedItem()));
        refreshViews();
    }

    private void refreshViews(){
//        studentGroupsView.getItems().clear();
//        teachersView.getItems().clear();
//        locationsView.getItems().clear();

        List<TeacherDTO> teachers = dataAccessProvider.getDataAccessContext().getTeacherDAO().getTeachers();
        teachersView.getItems().setAll(teachers);

        List<StudentGroupDTO> studentGroups = dataAccessProvider.getDataAccessContext().getStudentDAO().getStudentGroups();
        studentGroupsView.getItems().setAll(studentGroups);

        List<LocationDTO> locations = dataAccessProvider.getDataAccessContext().getLocationDAO().getLocations();
        locationsView.getItems().setAll(locations);
    }

    public void nameSelected(String tableName, SimpleDTO simpleDTO){
        if (simpleDTO == null){
            return;
        }
        updateTableContents(tableName, simpleDTO.getId());
        //doe nog iets?
    }

    public void clearTable(){
        gridPane.getChildren().removeAll(containerMap.values());
    }

    public void updateTableContents(String columnName, int id){
//        List<TeacherDTO> teachers = dataAccessProvider.getDataAccessContext().getTeacherDAO().getTeachers();
//        List<StudentGroupDTO> stugro = dataAccessProvider.getDataAccessContext().getStudentDAO().getStudentGroups();
//        List<LocationDTO> locations = dataAccessProvider.getDataAccessContext().getLocationDAO().getLocations();
//        for (TeacherDTO tdo : teachers){
//            System.out.println(tdo.getName());
//        }
//        for (LocationDTO ldo : locations){
//            System.out.println(ldo.getName());
//        }
//        for (StudentGroupDTO tdo : stugro){
//            System.out.println(tdo.getName());
//        }
//      DEBUG OUTPUT VOOR ALS DB SWITCH NIET WERKT


        LectureDAO lectureDAO = dataAccessProvider.getDataAccessContext().getLectureDAO();
        List<LectureDTO> lectureList = lectureDAO.getLecturesFromColumnById(columnName, id);
        //clear gridpane containerobjecten
        clearTable();
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
                TeacherDTO teacherById = dataAccessProvider.getDataAccessContext().getTeacherDAO().getTeacherByID(lec.getTeacher_id());
                if (teacherById == null){
                    showErrorDialog("One of the lectures uses a TeacherID for which there is no entry in table \"teachers\".");
                    return;
                }
                String teachname = dataAccessProvider.getDataAccessContext().getTeacherDAO().getTeacherByID(lec.getTeacher_id()).getName();
                LectureRepresentation lectureRepresentation = new LectureRepresentation(lec.getCourse(), teachname, lec);

                lectureRepresentation.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    //verwijder de styleclass van de vorige geselecteerde lecture
                    if (selected != null) {
                        selected.getStyleClass().remove("selectedlecture");
                    }
                    //update "selected" variabele en voeg stijlklasse toe
                    selected = (LectureRepresentation) event.getSource();
                    selected.getStyleClass().add("selectedlecture");
                });

                container.addLecture(lectureRepresentation);
                container.notifyOfChange();
            }
        }
    }

    public void openDatabase(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Database File");
        //chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite files", "*.db"));
        File file = chooser.showOpenDialog(
                gridPane.getScene().getWindow()
        );
        if ( file != null && file.getName().endsWith(".db")) {
            String path = file.getPath();
            updateDatabasePath(path);
            clearTable();
            initializeGridPaneRows();
            refreshViews();
        } else if (file != null){
            showErrorDialog("Please choose a file that has the .db extension.");
        }
    }

    public void updateDatabasePath(String path){
        //verander veld in dataAccessContext
        dataAccessProvider.setDbConnectionString(path);
        //reinitialize table
        refreshViews();
    }

    public void createDatabase(){
        DirectoryChooser chooser = new DirectoryChooser();
        File file = chooser.showDialog(gridPane.getScene().getWindow());
    }

    public void newEntry(){
        Map<String, SimpleDAO> titledPaneNameToTableName = new HashMap<>();
        titledPaneNameToTableName.put("Locations", dataAccessProvider.getDataAccessContext().getLocationDAO());
        titledPaneNameToTableName.put("Teachers", dataAccessProvider.getDataAccessContext().getTeacherDAO());
        titledPaneNameToTableName.put("Studentgroups", dataAccessProvider.getDataAccessContext().getStudentDAO());

        //zoeken welke pane van de accordion geopend is
        TitledPane openedPane = accordion.getExpandedPane();

        if (openedPane == null){
            //Geef aan dat er een pane geopend moet zijn
        } else {
            //naam v geopende pane vragen en omzetten naar tabelnaam waar we de nieuwe persoon moeten invoegen
            SimpleDAO simpleDAO = titledPaneNameToTableName.get(openedPane.getText());
            //naam die we moeten toevoegen aan DB
            String nameToAdd = newEntryTextField.getText();

            boolean addingEntryWasSucces = simpleDAO.addEntry(nameToAdd);
            if (! addingEntryWasSucces){
                showErrorDialog("Failed to add entry to database.");
            }
            refreshViews();
        }
    }

    public void renameEntry(){
        Map<String, ListView<SimpleDTO>> openedPaneNameToListView = new HashMap<>();
        openedPaneNameToListView.put("Locations", locationsView);
        openedPaneNameToListView.put("Teachers", teachersView);
        openedPaneNameToListView.put("Studentgroups", studentGroupsView);

        Map<String, SimpleDAO> tablenameToDAO = new HashMap<>();
        tablenameToDAO.put("location", dataAccessProvider.getDataAccessContext().getLocationDAO());
        tablenameToDAO.put("students", dataAccessProvider.getDataAccessContext().getStudentDAO());
        tablenameToDAO.put("teacher", dataAccessProvider.getDataAccessContext().getTeacherDAO());

        //vind welke pane open staat
        TitledPane openedPane = accordion.getExpandedPane();
        if (openedPane == null){
            showErrorDialog("Please select a location, studentgroup or teacher to rename.");
            return;
        }
        //zoek daarvan de selected entry met onze map
        SimpleDTO simpleDTO = openedPaneNameToListView.get(openedPane.getText()).getSelectionModel().getSelectedItem();
        if (simpleDTO == null){
            showErrorDialog("Please select a location, studentgroup or teacher to rename.");
            return;
        }
        //neem zijn ID en tabelnaam
        int id = simpleDTO.getId();
        String tableName = simpleDTO.getTableName();
        //krijg via de tabelnaam een DAO
        SimpleDAO dao = tablenameToDAO.get(tableName);
        //vraag aan die DAO om de naam aan te passen.
        //naam die we moeten toevoegen aan DB
        String newName = newEntryTextField.getText();

        boolean renamingEntryWasSuccess = dao.renameEntry(id, newName);

        if (! renamingEntryWasSuccess){
            showErrorDialog("Failed to rename entry.");
        }
        refreshViews();
        updateTableContents(tableName + "_id", id);
    }

    public void showErrorDialog(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText(message);

        alert.showAndWait();
    }
}

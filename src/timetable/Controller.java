/*
Van Braeckel Simon
 */

package timetable;

import dataaccessobjects.dataccessinterfaces.LectureDAO;
import databaseextra.DataAccessProvider;
import databaseextra.SQLiteDataAccessProvider;
import dataaccessobjects.dataccessinterfaces.SimpleDAO;
import datatransferobjects.*;
import guielements.LectureRepresentation;
import guielements.LectureContainer;
import guielements.MyTable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lectureinfodialog.LectureInput;
import starthourdialog.StartHourDialog;

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
    private List<String> starthours;

    //Alles ivm toevoegbar van lectures onderaan de applicatie
    public ComboBox<StudentGroupDTO> studentGroupComboBox;
    public ComboBox<TeacherDTO> teacherComboBox;
    public ComboBox<LocationDTO> locationComboBox;

    //veld dat geselecteerde lecture bijhoudt:
    private LectureRepresentation selectedLecture;


    public void initialize() {
        dataAccessProvider = new SQLiteDataAccessProvider();
        selectedLecture = null;
        initializeGridPaneRows();

        initializeViews();
        containerMap = new HashMap<>();
    }

    public void initializeGridPaneRows() {
        List<String> startHours = dataAccessProvider.getDataAccessContext().getPeriodDAO().getStartTimes();
        starthours = startHours;
        gridPane.initializeStartHours(startHours);
    }

    public void initializeViews() {
        teachersView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> nameSelected("teacher_id", teachersView.getSelectionModel().getSelectedItem()));

        studentGroupsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> nameSelected("students_id", studentGroupsView.getSelectionModel().getSelectedItem()));

        locationsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> nameSelected("location_id", locationsView.getSelectionModel().getSelectedItem()));
        refreshViews();
    }

    private void refreshViews() {
        List<TeacherDTO> teachers = dataAccessProvider.getDataAccessContext().getTeacherDAO().getTeachers();
        teachersView.getItems().setAll(teachers);
        teacherComboBox.getItems().addAll(teachers);

        List<StudentGroupDTO> studentGroups = dataAccessProvider.getDataAccessContext().getStudentDAO().getStudentGroups();
        studentGroupsView.getItems().setAll(studentGroups);
        studentGroupComboBox.getItems().setAll(studentGroups);

        List<LocationDTO> locations = dataAccessProvider.getDataAccessContext().getLocationDAO().getLocations();
        locationsView.getItems().setAll(locations);
        locationComboBox.getItems().setAll(locations);
    }

    public void nameSelected(String tableName, SimpleDTO simpleDTO) {
        selectedLecture = null;

        if (simpleDTO == null) {
            return;
        }
        updateTableContents(tableName, simpleDTO.getId());
        //doe nog iets?
    }

    public void clearTable() {
        gridPane.getChildren().removeAll(containerMap.values());
    }

    public void refreshTable(){
        updateTableContents(lastColumnName, lastId);
    }

    private String lastColumnName;
    private int lastId;
    public void updateTableContents(String columnName, int id) {
        lastColumnName = columnName;
        lastId = id;

        LectureDAO lectureDAO = dataAccessProvider.getDataAccessContext().getLectureDAO();
        List<LectureDTO> lectureList = lectureDAO.getLecturesFromColumnById(columnName, id);
        //clear gridpane containerobjecten
        clearTable();
        //clear map
        containerMap = new HashMap<>();
        for (LectureDTO lec : lectureList) {
            //loop over duration
            //als er nog geen entry is in de containermap voor die positie:
            //nieuwe lesuurcontainer maken en in map steken
            for (int i = 0; i < lec.getDuration(); i++) {
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
                if (teacherById == null) {
                    showErrorDialog("One of the lectures uses a TeacherID for which there is no entry in table \"teachers\".");
                    return;
                }
                String teachname = dataAccessProvider.getDataAccessContext().getTeacherDAO().getTeacherByID(lec.getTeacher_id()).getName();
                LectureRepresentation lectureRepresentation = new LectureRepresentation(lec.getCourse(), teachname, lec);

                lectureRepresentation.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    //verwijder de styleclass van de vorige geselecteerde lecture
                    if (selectedLecture != null) {
                        selectedLecture.getStyleClass().remove("selectedlecture");
                    }
                    //update "selectedLecture" variabele en voeg stijlklasse toe
                    selectedLecture = (LectureRepresentation) event.getSource();
                    selectedLecture.getStyleClass().add("selectedlecture");
                });

                container.addLecture(lectureRepresentation);
                container.notifyOfChange();
            }
        }
    }

    public void openDatabase() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Database File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Sqlite Files (.db)", "*.db"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = chooser.showOpenDialog(
                gridPane.getScene().getWindow()
        );
        if (file != null && file.getName().endsWith(".db")) {
            String path = file.getPath();
            updateDatabasePath(path);
            clearTable();
            initializeGridPaneRows();
            refreshViews();
        } else if (file != null) {
            showErrorDialog("Please choose a file that has the .db extension.");
        }
    }

    public void updateDatabasePath(String path) {
        //verander veld in dataAccessContext
        dataAccessProvider.setDbConnectionString(path);
        //reinitialize table
        refreshViews();
    }

    public void createDatabase() {
        StartHourDialog startHourDialog = new StartHourDialog();
        startHourDialog.showAndWait();
        List<Integer[]> startHours = startHourDialog.getStartHours();
        if (startHours == null) { //Dit zou betekenen dat er op cancel is gedrukt in het dialoogvenster
            return;
        }

        FileChooser chooser = new FileChooser();
        Window parent = gridPane.getScene().getWindow();
        chooser.setTitle("Create database dialog.");
        File destinationFile = chooser.showSaveDialog(parent);
        if (destinationFile != null) {
            String path = destinationFile.getPath().endsWith(".db") ? destinationFile.getPath() : destinationFile.getPath() + ".db";
            dataAccessProvider.setDbConnectionString(path);
            dataAccessProvider.getDataAccessContext().getDatabaseDefiner().define(startHours);

            clearTable();
            initializeGridPaneRows();
            refreshViews();
        } else {
            return;
        }
    }

    public void newEntry(){
        //Ingegeven naam controleren
        if (newEntryTextField.getText().contains("\"") || newEntryTextField.getText().isEmpty()){
            showErrorDialog("Invalid name.");
            return;
        }


        Map<String, SimpleDAO> titledPaneNameToTableName = new HashMap<>();
        titledPaneNameToTableName.put("Locations", dataAccessProvider.getDataAccessContext().getLocationDAO());
        titledPaneNameToTableName.put("Teachers", dataAccessProvider.getDataAccessContext().getTeacherDAO());
        titledPaneNameToTableName.put("Studentgroups", dataAccessProvider.getDataAccessContext().getStudentDAO());

        //zoeken welke pane van de accordion geopend is
        TitledPane openedPane = accordion.getExpandedPane();

        if (openedPane == null){
            showErrorDialog("Open a category to add this entry to.");
        } else {
            //naam v geopende pane vragen en omzetten naar tabelnaam waar we de nieuwe persoon moeten invoegen
            SimpleDAO simpleDAO = titledPaneNameToTableName.get(openedPane.getText());
            //naam die we moeten toevoegen aan DB
            String nameToAdd = newEntryTextField.getText();

            boolean addingEntryWasSucces = simpleDAO.addEntry(nameToAdd);
            if (! addingEntryWasSucces){
                showErrorDialog("Failed to add entry.");
            }
            refreshViews();
        }
    }

    public void renameEntry(){
        if (newEntryTextField.getText().contains("\"") || newEntryTextField.getText().isEmpty()){
            showErrorDialog("Invalid name.");
            return;
        }

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
        //zoek daarvan de selectedLecture entry met onze map
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

    public void deleteLecture(){
        if (selectedLecture == null){
            showErrorDialog("A lecture must be selected.");
            return;
        }

         if (! dataAccessProvider.getDataAccessContext().getLectureDAO().deleteEntry(selectedLecture.getLectureDTO())){
             showErrorDialog("Failed to delete lecture.");
             return;
         }

         refreshTable();
    }

    public void editLecture(){
        if (selectedLecture == null){
            showErrorDialog("A lecture must be selected");
            return;
        }

        LectureInput lectureInput = new LectureInput(studentGroupsView.getItems(), teachersView.getItems(), locationsView.getItems(), starthours, selectedLecture);
        lectureInput.showAndWait();
        LectureDTO lectureDTO = lectureInput.getLectureDTO();

        if (lectureDTO != null) {
            //oude lecture verwijderen
            dataAccessProvider.getDataAccessContext().getLectureDAO().deleteEntry(selectedLecture.getLectureDTO());
            selectedLecture = null;

            //adhv nieuwe lectureDTO een entry toevoegen
            if (dataAccessProvider.getDataAccessContext().getLectureDAO().addEntry(lectureDTO)) {
                refreshTable();
            } else {
                showErrorDialog("Failed to add entry.");
            }
        }
    }

    public void createLecture(){
        LectureInput lectureInput = new LectureInput(studentGroupsView.getItems(), teachersView.getItems(), locationsView.getItems(), starthours, null);
        lectureInput.showAndWait();
        LectureDTO lectureDTO = lectureInput.getLectureDTO();

        if (lectureDTO != null) {
            //adhv deze lectureDTO een entry toevoegen
            if (dataAccessProvider.getDataAccessContext().getLectureDAO().addEntry(lectureDTO)) {
                refreshTable();
            } else {
                showErrorDialog("Failed to add entry.");
            }
        }
    }

    public void showErrorDialog(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText(message);

        alert.showAndWait();
    }
}

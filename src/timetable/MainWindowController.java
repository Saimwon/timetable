/*
Van Braeckel Simon
 */

package timetable;

import databasemanipulation.databaseextra.DataAccessContext;
import databasemanipulation.databaseextra.DataAccessProvider;
import databasemanipulation.databaseextra.SQLiteDataAccessProvider;
import databasemanipulation.dataaccessobjects.dataccessinterfaces.SimpleDAO;
import datatransferobjects.*;
import guielements.ErrorDialog;
import guielements.LectureRepresentation;
import guielements.LectureContainer;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import lectureinfodialog.EditLectureInput;
import lectureinfodialog.LectureInput;
import lectureinfodialog.LectureInputController;
import starthourdialog.StartHourDialog;
import usagewindow.HelpWindow;

import java.io.File;
import java.util.*;

public class MainWindowController {
    private TimetableModel timetableModel;
    private DataAccessProvider dataAccessProvider;

    public TextField newEntryTextField;
    public Button newEntryButton;
    public Button renameEntryButton;
    public TimetableView gridPane;
    public Accordion accordion;

    public ListView<StudentGroupDTO> studentGroupsView;
    public ListView<TeacherDTO> teachersView;
    public ListView<LocationDTO> locationsView;

    private Map<String, LectureContainer> containerMap;

    private LectureRepresentation selectedLecture;

    public void initialize() {
        dataAccessProvider = new SQLiteDataAccessProvider();
        timetableModel = new TimetableModel(dataAccessProvider, this, null);
        gridPane.setModel(timetableModel);

        studentGroupsView.itemsProperty().bind(timetableModel.studentGroupDTOListProperty());
        teachersView.itemsProperty().bind(timetableModel.teacherDTOListProperty());
        locationsView.itemsProperty().bind(timetableModel.locationDTOListProperty());

        selectedLecture = null;
        addListViewEventListeners();
        containerMap = new HashMap<>();
    }

    private void addListViewEventListeners() {
        teachersView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> onNameSelected("teacher_id", teachersView.getSelectionModel().getSelectedItem()));

        studentGroupsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> onNameSelected("students_id", studentGroupsView.getSelectionModel().getSelectedItem()));

        locationsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> onNameSelected("location_id", locationsView.getSelectionModel().getSelectedItem()));
    }

    private boolean databaseValid(){
        DataAccessContext dataAccessContext = dataAccessProvider.getDataAccessContext();

        List<TeacherDTO> teachers = dataAccessContext.getTeacherDAO().getAllEntries();
        if (teachers == null) {
            return false;
        }
        List<StudentGroupDTO> studentGroups = dataAccessContext.getStudentDAO().getAllEntries();
        if (studentGroups == null) {
            return false;
        }
        List<LocationDTO> locations = dataAccessContext.getLocationDAO().getAllEntries();

        return locations != null && dataAccessContext.getLectureDAO().tableExists();
    }

    private void refreshListViews() {
//        timetableModel.refreshListViews();
        DataAccessContext dataAccessContext = dataAccessProvider.getDataAccessContext();

        //refreshListView(dataAccessContext.getStudentDAO(), studentGroupsView);

        List<TeacherDTO> teachers = dataAccessContext.getTeacherDAO().getAllEntries();
        List<StudentGroupDTO> studentGroups = dataAccessContext.getStudentDAO().getAllEntries();
        List<LocationDTO> locations = dataAccessContext.getLocationDAO().getAllEntries();

        teachersView.getItems().setAll(teachers);
        studentGroupsView.getItems().setAll(studentGroups);
        locationsView.getItems().setAll(locations);
    }

    private <T> void refreshListView(SimpleDAO<T> simpleDAO, ListView<T> listView){
        listView.getItems().setAll(simpleDAO.getAllEntries());
    }

    private void onNameSelected(String tableName, SimpleDTO simpleDTO) {
        selectedLecture = null;

        if (simpleDTO == null) {
            return;
        }
        timetableModel.updateTableContents(tableName, simpleDTO.getId());
    }

    private void clearTable() {
        gridPane.getChildren().removeAll(containerMap.values());
    }

    public void onLectureSelected(LectureRepresentation lectureRepresentation){
        if (selectedLecture != null) {
            for (LectureRepresentation lecture : selectedLecture.getLectureGroup()) {
                lecture.getStyleClass().remove("selectedlecture");
            }
        }
        this.selectedLecture = lectureRepresentation;
        for (LectureRepresentation lecture : lectureRepresentation.getLectureGroup()) {
            lecture.getStyleClass().add("selectedlecture");
        }
    }

    public void openDatabase() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Database File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Sqlite Files (.db)", "*.db"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = chooser.showOpenDialog(gridPane.getScene().getWindow());
        if (file != null && file.getName().endsWith(".db")) {
            String oldDatabaseConnectionString = dataAccessProvider.getDbConnectionString();
            String newPath = file.getPath();

            dataAccessProvider.setDbConnectionString(newPath);

            if (! databaseValid()){
                showErrorDialog("This is not a valid database.");
                return;
            }

            refreshListViews();
            clearTable();
            timetableModel.updateStarthours();
        } else if (file != null) {
            showErrorDialog("Please choose a file that has the .db extension.");
        }
    }

    public void createDatabase() {
        StartHourDialog startHourDialog = new StartHourDialog();
        startHourDialog.initOwner(gridPane.getScene().getWindow());
        startHourDialog.showAndWait();

        List<Integer[]> newStartHours = startHourDialog.getStartHours();
        if (newStartHours == null) { //Dit zou betekenen dat er op cancel is gedrukt in het dialoogvenster
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Create database.");
        File destinationFile = chooser.showSaveDialog(gridPane.getScene().getWindow());

        if (destinationFile != null) {
            String path = destinationFile.getPath().endsWith(".db") ? destinationFile.getPath() : destinationFile.getPath() + ".db";
            dataAccessProvider.setDbConnectionString(path);
            dataAccessProvider.getDataAccessContext().getDatabaseDefiner().define(newStartHours);

            clearTable();
            timetableModel.updateStarthours();
            timetableModel.clearListViews();
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
            //naam die we moeten toevoegen ophalen
            String nameToAdd = newEntryTextField.getText();

            //Block deze operatie als er al een entry bestaat met deze naam:
            if (! simpleDAO.getEntryByName(nameToAdd).isEmpty()){
                showErrorDialog("Entries with duplicate names are not allowed.");
                return;
            }

            boolean addingEntryWasSucces = simpleDAO.addEntry(nameToAdd);
            if (! addingEntryWasSucces){
                showErrorDialog("Failed to add entry.");
            }

            refreshListViews();
        }
    }

    public void renameEntry(){
        if (newEntryTextField.getText().contains("\"") || newEntryTextField.getText().isEmpty()){
            showErrorDialog("Invalid name.");
            return;
        }

        Map<ListView<? extends SimpleDTO>, SimpleDAO> listViewSimpleDAOMap = new HashMap<>();
        listViewSimpleDAOMap.put(studentGroupsView, dataAccessProvider.getDataAccessContext().getStudentDAO());
        listViewSimpleDAOMap.put(teachersView, dataAccessProvider.getDataAccessContext().getTeacherDAO());
        listViewSimpleDAOMap.put(locationsView, dataAccessProvider.getDataAccessContext().getLocationDAO());

        Map<String, ListView<? extends SimpleDTO>> openedPaneNameToListView = new HashMap<>();
        openedPaneNameToListView.put("Locations", locationsView);
        openedPaneNameToListView.put("Teachers", teachersView);
        openedPaneNameToListView.put("Studentgroups", studentGroupsView);

        //vind welke pane open staat
        TitledPane openedPane = accordion.getExpandedPane();
        if (openedPane == null){
            showErrorDialog("Please select a location, studentgroup or teacher to rename.");
            return;
        }
        //Neem de bijhorende listview en dataAccessObject
        ListView<? extends SimpleDTO> listView = openedPaneNameToListView.get(openedPane.getText());
        SimpleDAO simpleDAO = listViewSimpleDAOMap.get(listView);

        //Neem geselecteerde DTO
        SimpleDTO simpleDTO = listView.getSelectionModel().getSelectedItem();
        if (simpleDTO == null){
            showErrorDialog("Please select a location, studentgroup or teacher to rename.");
            return;
        }
        //neem zijn ID en tabelnaam
        int id = simpleDTO.getId();

        //Voeg naam toe aan DB
        String newName = newEntryTextField.getText();
        boolean renamingEntryWasSuccess = simpleDAO.renameEntry(id, newName);

        if (! renamingEntryWasSuccess){
            showErrorDialog("Failed to rename entry.");
            return;
        }

        refreshListViews();
        timetableModel.refreshTable();
    }

    public void createLecture(){
        LectureInput lectureInput = new LectureInput(studentGroupsView.getItems(), teachersView.getItems(), locationsView.getItems(), timetableModel.getStartHours(), new LectureInputController());

        lectureInput.initOwner(gridPane.getScene().getWindow());
        lectureInput.showAndWait();

        LectureDTO lectureDTO = lectureInput.getResultLectureDTO();

        if (lectureDTO != null) {
            //adhv deze lectureDTO een entry toevoegen
            if (dataAccessProvider.getDataAccessContext().getLectureDAO().addEntryIfNotExists(lectureDTO)) {
                timetableModel.refreshTable();
            } else {
                showErrorDialog("Failed to add entry.");
            }
        }
    }

    public void editLecture(){
        if (selectedLecture == null){
            showErrorDialog("A lecture must be selected.");
            return;
        }
        editLecture(selectedLecture);
    }

    public void editLecture(LectureRepresentation source){
        LectureInput lectureInput = new EditLectureInput(studentGroupsView.getItems(), teachersView.getItems(),
                locationsView.getItems(), timetableModel.getStartHours(), source);
        lectureInput.initOwner(gridPane.getScene().getWindow());
        lectureInput.showAndWait();

        LectureDTO newLectureDTO = lectureInput.getResultLectureDTO();
        LectureDTO selectedLectureDTO = source.getLectureDTO();

        if (newLectureDTO != null && ! newLectureDTO.equals(selectedLectureDTO)) {
            //oude lecture verwijderen
            dataAccessProvider.getDataAccessContext().getLectureDAO().deleteEntry(selectedLectureDTO);
            selectedLecture = null;

            //adhv nieuwe lectureDTO een entry toevoegen
            if (dataAccessProvider.getDataAccessContext().getLectureDAO().addEntryIfNotExists(newLectureDTO)) {
                timetableModel.refreshTable();
            } else {
                showErrorDialog("Failed to add entry.");
            }
        }
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

        timetableModel.refreshTable();
    }

    private void showErrorDialog(String message){
        ErrorDialog timedErrorDialog = new ErrorDialog(message, gridPane.getScene().getWindow());
        timedErrorDialog.showAndWait();
    }

    public void showHelpWindow(){
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.initOwner(gridPane.getScene().getWindow());
        helpWindow.show();
    }
}

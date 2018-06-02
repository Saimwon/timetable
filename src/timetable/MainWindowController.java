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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.DataFormat;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lectureinfodialog.EditLectureInputController;
import lectureinfodialog.LectureInput;
import lectureinfodialog.LectureInputController;
import starthourdialog.StartHourDialog;
import usagewindow.HelpWindow;

import java.io.File;
import java.util.*;
import java.util.function.Supplier;

/*
Controller voor het hoofdvenster van het programma.
 */
public class MainWindowController {
    public static DataFormat CUSTOM_LECTUREDTO = new DataFormat("LectureDTO");

    private TimetableModel timetableModel;
    private ListViewModel listViewModel;
    private DataAccessProvider dataAccessProvider;

    public TextField newEntryTextField;
    public Button newEntryButton;
    public Button renameEntryButton;
    public TimetableView gridPane;
    public Accordion accordion;

    public ListView<StudentGroupDTO> studentGroupsView;
    public ListView<TeacherDTO> teachersView;
    public ListView<LocationDTO> locationsView;

    private LectureRepresentation selectedLecture;
    private LectureInput lectureInput;

    public void initialize() {
        dataAccessProvider = new SQLiteDataAccessProvider();
        timetableModel = new TimetableModel(dataAccessProvider, this);
        gridPane.setModel(timetableModel);

        listViewModel = new ListViewModel(dataAccessProvider);
        studentGroupsView.itemsProperty().bind(listViewModel.studentGroupDTOListProperty());
        teachersView.itemsProperty().bind(listViewModel.teacherDTOListProperty());
        locationsView.itemsProperty().bind(listViewModel.locationDTOListProperty());

        lectureInput = null;

        selectedLecture = null;
        addListViewEventListeners();
        gridPane.setMainWindowController(this);
    }

    /*
    Add event listeners to detect when a listview element is selected.
     */
    private void addListViewEventListeners() {
        teachersView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                onNameSelected("teacher_id", teachersView.getSelectionModel().getSelectedItem()));

        studentGroupsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                onNameSelected("students_id", studentGroupsView.getSelectionModel().getSelectedItem()));

        locationsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                onNameSelected("location_id", locationsView.getSelectionModel().getSelectedItem()));
    }

    /*
    Methode die checkt of een databank de juiste tabellen bevat.
     */
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

    /*
    Opgeroepen wanneer men iets selecteert in de listviews, zorgt dat selectedlecture null wordt.
     */
    private void onNameSelected(String tableName, SimpleDTO simpleDTO) {
        selectedLecture = null;

        if (simpleDTO == null) {
            return;
        }
        timetableModel.updateTableContents(tableName, simpleDTO.getId());
    }

    /*
    Verwijder stijlklasse van voriggeselecteerde lecturerepresentations en voegt toe aan de nieuwgeselecteerde.
     */
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

    /*
    Selecteer een databank om te openen.
     */
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

            listViewModel.refreshListViews();
            timetableModel.clearTableContents();
            timetableModel.updateStarthours();
        } else if (file != null) {
            showErrorDialog("Please choose a file that has the .db extension.");
        }
    }

    /*
    Kies starturen en een locatie voor een nieuwe Databank
    Maak de nieuwe databank aan en laad hem in.
     */
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

            timetableModel.clearTableContents();
            timetableModel.updateStarthours();
            listViewModel.clearListViews();
        }
    }

    /*
    Check of de text in het toevoegtextveld rechts niet leeg is en geen naam bevat die al in de databank bestaat.
     */
    private boolean checkTextField(SimpleDAO dao){
        String newName = newEntryTextField.getText();
        if (newName.isEmpty()) {
            showErrorDialog("Invalid name.");
            return false;
        }
        if (! dao.getEntryByName(newName).isEmpty()) {
            showErrorDialog("An entry with this name already exists.");
            return false;
        }
        return true;
    }

    /*
    Methode die wordt opgeroepen wanneer men op Add Entry of Rename Entry drukt. Was oorspronkelijk opgesplitst in
    2 methoden maar dat gaf veel duplicatie van code. Vandaar deze aanpak met ifs en de UserData.
     */
    public void editEntry(ActionEvent event) {
        Button butt = (Button) event.getSource();
        boolean rename = butt.getUserData().equals("rename");

        List<String> errorMessages = fillErrorMessages(rename);

        Map<String, SimpleDAO> titledPaneNameToDAO = new HashMap<>();
        Map<String, ListView<? extends SimpleDTO>> openedPaneNameToListView = new HashMap<>();
        Map<ListView<? extends SimpleDTO>, SimpleDAO> listViewSimpleDAOMap = new HashMap<>();
        SimpleDAO dao = null;
        SimpleDTO dto = null;
        int id = -1;

        String newName = newEntryTextField.getText();

        titledPaneNameToDAO.put("Locations", dataAccessProvider.getDataAccessContext().getLocationDAO());
        titledPaneNameToDAO.put("Teachers", dataAccessProvider.getDataAccessContext().getTeacherDAO());
        titledPaneNameToDAO.put("Studentgroups", dataAccessProvider.getDataAccessContext().getStudentDAO());

        try {
            String openedPaneName = accordion.getExpandedPane().getText();
            dao = titledPaneNameToDAO.get(openedPaneName);

            if (rename) {
                openedPaneNameToListView.put("Locations", locationsView);
                openedPaneNameToListView.put("Teachers", teachersView);
                openedPaneNameToListView.put("Studentgroups", studentGroupsView);

                ListView<? extends SimpleDTO> lijst = openedPaneNameToListView.get(openedPaneName);
                dto = lijst.getSelectionModel().getSelectedItem();
                id = dto.getId();
            }

        } catch (NullPointerException e) { //Komt voor als er geen TitledPane geopend is in de Accordion of als er geen naam geselecteerd was.
            showErrorDialog(errorMessages.get(0));
            return;
        }

        if (checkTextField(dao)){
            if (rename ? !dao.renameEntry(id, newName) : !dao.addEntry(newName)) {
                showErrorDialog(errorMessages.get(1));
                return;
            }
        }

        listViewModel.refreshListViews();
        if (rename) timetableModel.refreshTable();
    }

    /*
    Geeft een lijst terug met de errormessages die nodig zijn in de editentry methode
     */
    private List<String> fillErrorMessages(boolean rename) {
        List<String> result = new ArrayList<>();
        if (rename){
            result.add("Please select a location, studentgroup or teacher to rename.");
            result.add("Failed to rename entry.");
        } else {
            result.add("Please open a category to add this entry to.");
            result.add("Failed to add entry.");
        }

        return result;
    }

    /*
    Opent venster om input te geven voor de nieuwe lecture
    Neemt resultaat uit LectureInput
    Voegt lecture toe aan de databank
     */
    public void createLecture(){
        if (lectureInput != null){
            lectureInput.close();
            try {
                Thread.sleep(500);
            } catch (Exception ignored){}
            lectureInput = null;
        }

        LectureInput lectureInput = new LectureInput(listViewModel, timetableModel.getStartHours(),
                new LectureInputController(), "New Lecture.");

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

    /*
    Methode die editLecture oproept met de geselecteerde lecture als er geen parameters worden meegegeven.
     */
    public void editLecture(double xCo, double yCo){
        if (selectedLecture == null){
            showErrorDialog("A lecture must be selected.");
            return;
        }
        editLecture(xCo, yCo, selectedLecture);
    }

    /*
    Opent venster om input te geven om de lecture mee aan te passen
    Neemt resultaat uit LectureInput
    Voegt lecture toe aan de databank
     */
    public void editLecture(double xCo, double yCo, LectureRepresentation source){
        lectureInput = new LectureInput(listViewModel, timetableModel.getStartHours(),
                new EditLectureInputController(source), "Edit Lecture");
        lectureInput.setX(xCo);
        lectureInput.setY(yCo);
        lectureInput.initOwner(gridPane.getScene().getWindow());
        lectureInput.showAndWait();

        LectureDTO newLectureDTO = lectureInput.getResultLectureDTO();
        LectureDTO selectedLectureDTO = source.getLectureDTO();

        commitLectureEdit(selectedLectureDTO, newLectureDTO);
    }

    public void commitLectureEdit(LectureDTO oldLectureDTO, LectureDTO newLectureDTO){
        if (newLectureDTO != null && ! newLectureDTO.equals(oldLectureDTO)) {
            //oude lecture verwijderen
            dataAccessProvider.getDataAccessContext().getLectureDAO().deleteEntry(oldLectureDTO);
            selectedLecture = null;

            //adhv nieuwe lectureDTO een entry toevoegen
            if (dataAccessProvider.getDataAccessContext().getLectureDAO().addEntryIfNotExists(newLectureDTO)) {
                timetableModel.refreshTable();
            } else {
                showErrorDialog("Failed to edit entry.");
            }
        }
    }

    /*
    Verwijdert geselecteerde lecture.
     */
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

    /*
    Toont een errorboodschap met de gegeven message
     */
    private void showErrorDialog(String message){
        ErrorDialog timedErrorDialog = new ErrorDialog(message, gridPane.getScene().getWindow());
        timedErrorDialog.showAndWait();
    }

    /*
    Toont het venster waar de controls uitgelegd staan.
     */
    public void showHelpWindow(){
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.initOwner(gridPane.getScene().getWindow());
        helpWindow.show();
    }

    public TimetableModel getTimetableModel(){
        return this.timetableModel;
    }
}

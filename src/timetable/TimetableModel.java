package timetable;

import databasemanipulation.dataaccessobjects.dataccessinterfaces.LectureDAO;
import databasemanipulation.dataaccessobjects.dataccessinterfaces.SimpleDAO;
import databasemanipulation.databaseextra.DataAccessProvider;
import datatransferobjects.*;
import guielements.LectureRepresentation;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class TimetableModel implements Observable {
    private DataAccessProvider dataAccessProvider;
    private static int AANTAL_DAGEN = 5;
    private List<String> startHours;
    private MainWindowController mainWindowController;

    private ListProperty<StudentGroupDTO> studentGroupDTOList;
    private ListProperty<TeacherDTO> teacherDTOList;
    private ListProperty<LocationDTO> locationDTOList;

    private List<List<ObservableList<LectureRepresentation>>> table;

//moet in initialize al de juiste kolommen toevoegen
    public TimetableModel(DataAccessProvider dataAccessProvider, MainWindowController mainWindowController, ObservableList<SimpleDTO>[] observableLists) {
        this.dataAccessProvider = dataAccessProvider;
        this.mainWindowController = mainWindowController;

        studentGroupDTOList = new SimpleListProperty<>();
        teacherDTOList = new SimpleListProperty<>();
        locationDTOList = new SimpleListProperty<>();

        table = new ArrayList<>();
        //steek kolommen in table
        for (int i = 0; i < AANTAL_DAGEN; i++){
            table.add(new ArrayList<>());
        }
    }

    public void updateStarthours(){
        List<String> startHours = dataAccessProvider.getDataAccessContext().getPeriodDAO().getStartTimes();
        this.startHours = startHours;
        this.setStartHours(startHours);
    }

    private void setStartHours(List<String> startUren){
        this.startHours = startHours;

        for (int j = 0; j < table.size(); j ++) {
            table.get(j).clear();
            for (int i = 0; i < startUren.size(); i++) {
                ObservableList<LectureRepresentation> observableList = FXCollections.observableArrayList();

                table.get(j).add(observableList);
            }
        }

        fireInvalidationEvent();
    }

    private String lastColumnName;
    private int lastId;
    public void updateTableContents(String columnName, int id){
        /*
        Wat moet er hier nog gebeuren?
        Waar maken we de lijsten aan en delen we lectures op in deelLectures?
         */


        lastColumnName = columnName;
        lastId = id;

        LectureDAO lectureDAO = dataAccessProvider.getDataAccessContext().getLectureDAO();
        List<LectureDTO> lectureList = lectureDAO.getLecturesFromColumnById(columnName, id);

        clearTableContents();

        for (LectureDTO lectureDTO : lectureList) {
            List<LectureRepresentation> lectureGroup = new ArrayList<>();
            //loop over duration
            for (int i = 0; i < lectureDTO.getDuration(); i++) {
                int row = lectureDTO.getFirst_block() + i;
                int column = lectureDTO.getDay();

                //Vraag de naam van de prof op om in de lectureRepresentation te zetten.
                TeacherDTO teacherById = dataAccessProvider.getDataAccessContext().getTeacherDAO().getEntryById(lectureDTO.getTeacher_id());
                if (teacherById == null) {
                    continue;
                }

                LectureRepresentation lectureRepresentation = new LectureRepresentation(lectureDTO.getCourse(), teacherById.getName(), lectureDTO, mainWindowController);
                lectureRepresentation.setLectureGroup(lectureGroup);

                table.get(column-1).get(row-1).add(lectureRepresentation);
                lectureGroup.add(lectureRepresentation);
            }
        }
    }

    private void clearTableContents(){
        for (List<ObservableList<LectureRepresentation>> column: table){
            for (ObservableList<LectureRepresentation> observableList : column){
                observableList.clear();
            }
        }
    }

    public List<List<ObservableList<LectureRepresentation>>> getTable(){
        return this.table;
    }

    public void refreshTable(){
        updateTableContents(lastColumnName, lastId);
    }

    public void refreshListViews(){
        refreshListView(dataAccessProvider.getDataAccessContext().getStudentDAO(), studentGroupDTOS);
        refreshListView(dataAccessProvider.getDataAccessContext().getTeacherDAO(), teacherDTOS);
        refreshListView(dataAccessProvider.getDataAccessContext().getLocationDAO(), locationDTOS);
    }

    public void clearListViews(){
        studentGroupDTOS.clear();
        teacherDTOS.clear();
        locationDTOS.clear();
    }

    private <T> void refreshListView(SimpleDAO<T> simpleDAO, ObservableList<T> observableList) {
        observableList.setAll(simpleDAO.getAllEntries());
    }

    public ListProperty<StudentGroupDTO> studentGroupDTOListProperty() {
        return studentGroupDTOList;
    }

    public ListProperty<TeacherDTO> teacherDTOListProperty() {
        return teacherDTOList;
    }

    public ListProperty<LocationDTO> locationDTOListProperty() {
        return locationDTOList;
    }

    public List<String> getStartHours() {
        return startHours;
    }


    //Luisteraargerelateerde dingen:
    /**
     * Lijst van geregistreerde luisteraars.
     */
    private List<InvalidationListener> listenerList = new ArrayList<> ();

    /**
     * Breng alle luisteraars op de hoogte van een verandering van het model.
     */
    private void fireInvalidationEvent () {
        for (InvalidationListener listener : listenerList) {
            listener.invalidated(this);
        }
    }

    @Override
    public void addListener(InvalidationListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listenerList.remove(listener);
    }
}

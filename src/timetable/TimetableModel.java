package timetable;

import dataaccessobjects.dataccessinterfaces.LectureDAO;
import databaseextra.DataAccessContext;
import databaseextra.DataAccessProvider;
import datatransferobjects.LectureDTO;
import datatransferobjects.TeacherDTO;
import guielements.LectureContainer;
import guielements.LectureRepresentation;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.*;

public class TimetableModel implements Observable {
    private DataAccessProvider dataAccessProvider;
    private static int AANTAL_DAGEN = 5;
    private List<String> startHours;
    private Controller controller;

    private List<List<LectureRepresentation>> lectureGroups;
    private List<List<ObservableList<LectureRepresentation>>> table;

//moet in initialize al de juiste kolommen toevoegen
    public TimetableModel(DataAccessProvider dataAccessProvider, Controller controller){
        this.dataAccessProvider = dataAccessProvider;
        this.startHours = dataAccessProvider.getDataAccessContext().getPeriodDAO().getStartTimes();
        this.controller = controller;

        this.lectureGroups = new ArrayList<>();

        table = new ArrayList<>();
        //steek kolommen in table
        for (int i = 0; i < AANTAL_DAGEN; i++){
            table.add(new ArrayList<>());
        }

        this.setStartHours(this.startHours);
    }

    public void updateStarthours(){
        List<String> startHours = dataAccessProvider.getDataAccessContext().getPeriodDAO().getStartTimes();
        this.startHours = startHours;
        this.setStartHours(startHours);
    }

    public void setStartHours(List<String> startUren){
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
            this.lectureGroups.add(lectureGroup);
            //loop over duration
            for (int i = 0; i < lectureDTO.getDuration(); i++) {
                int row = lectureDTO.getFirst_block() + i;
                int column = lectureDTO.getDay();

                TeacherDTO teacherById = dataAccessProvider.getDataAccessContext().getTeacherDAO().getEntryById(lectureDTO.getTeacher_id());
                if (teacherById == null) {
//showErrorDialog("One of the lectures uses a TeacherID for which there is no entry in table \"teachers\".");
                    continue;
                }

                //zet lecture in lijst
                LectureRepresentation lectureRepresentation = new LectureRepresentation(lectureDTO.getCourse(), teacherById.getName(), lectureDTO, controller);
                lectureRepresentation.setLectureGroup(lectureGroup);

                table.get(column-1).get(row-1).add(lectureRepresentation);
                lectureGroup.add(lectureRepresentation);
            }
        }
        checkTable();
    }

    private void clearTableContents(){
        for (List<ObservableList<LectureRepresentation>> column: table){
            for (ObservableList<LectureRepresentation> observableList : column){
                observableList.clear();
            }
        }
    }
    private void checkTable(){
        for (int i = 0 ; i < table.size(); i++){
            for (int j = 0; j < table.get(i).size(); j++){
//                System.out.println("Plaats: " + i + " " + j + ":");
//                for (LectureRepresentation lectureRepresentation : table.get(i).get(j)){
//                    System.out.println(lectureRepresentation.getCourseName());
//                }
//                System.out.println("-----------------------------------------------");
            }
        }
    }

    public List<List<ObservableList<LectureRepresentation>>> getTable(){
        return this.table;
    }

    public void refreshTable(){
        updateTableContents(lastColumnName, lastId);
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

    public List<String> getStartHours() {
        return startHours;
    }
}

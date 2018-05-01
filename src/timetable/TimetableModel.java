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

    private List<List<ObservableList<LectureRepresentation>>> table;

//moet in initialize al de juiste kolommen toevoegen
    public TimetableModel(List<String> startUren, DataAccessProvider dataAccessProvider){
        this.startHours = startUren;

        this.dataAccessProvider = dataAccessProvider;

        table = new ArrayList<>();
        //steek kolommen in table
        for (int i = 0; i < AANTAL_DAGEN; i++){
            table.add(new ArrayList<>());
        }

        //Steek in elke arraylist het starturen aantal observablelists
        setStartHours(startUren);
    }

    public void updateStarthours(){
        List<String> startHours = dataAccessProvider.getDataAccessContext().getPeriodDAO().getStartTimes();
        this.startHours = startHours;
        this.setStartHours(startHours);
    }

    public void setStartHours(List<String> startUren){
        this.startHours = startHours;

        for (int j = 0; j < table.size(); j ++) {
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
        lastColumnName = columnName;
        lastId = id;

        LectureDAO lectureDAO = dataAccessProvider.getDataAccessContext().getLectureDAO();
        List<LectureDTO> lectureList = lectureDAO.getLecturesFromColumnById(columnName, id);

        clearTable();
        //clear map
        for (LectureDTO lectureDTO : lectureList) {
            //loop over duration
            for (int i = 0; i < lectureDTO.getDuration(); i++) {
                int row = lectureDTO.getDay();
                int column = lectureDTO.getFirst_block() + i;

                //zet lecture in container
                TeacherDTO teacherById = dataAccessProvider.getDataAccessContext().getTeacherDAO().getEntryById(lectureDTO.getTeacher_id());
                if (teacherById == null) {
//showErrorDialog("One of the lectures uses a TeacherID for which there is no entry in table \"teachers\".");
                    return;
                }

                //zet lecture in lijst

                table.get(row-1).get(column-1).add(new LectureRepresentation(lectureDTO.getCourse(), teacherById.getName(), lectureDTO));
            }
            String teachname = dataAccessProvider.getDataAccessContext().getTeacherDAO().getEntryById(lectureDTO.getTeacher_id()).getName();
            LectureRepresentation lectureRepresentation = new LectureRepresentation(lectureDTO.getCourse(), teachname, lectureDTO);
        }
        checkTable();
    }

    private void clearTable(){
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

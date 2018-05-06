/*
Simon Van Braeckel
 */

package timetable;

import databasemanipulation.dataaccessobjects.dataccessinterfaces.SimpleDAO;
import databasemanipulation.databaseextra.DataAccessContext;
import databasemanipulation.databaseextra.DataAccessProvider;
import datatransferobjects.LocationDTO;
import datatransferobjects.StudentGroupDTO;
import datatransferobjects.TeacherDTO;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class ListViewModel {
    private DataAccessProvider dataAccessProvider;

    /*
    Representations of listviewcontent.
     */
    private ListProperty<StudentGroupDTO> studentGroupDTOList;
    private ListProperty<TeacherDTO> teacherDTOList;
    private ListProperty<LocationDTO> locationDTOList;

    public ListViewModel(DataAccessProvider dataAccessProvider) {
        this.dataAccessProvider = dataAccessProvider;

        this.studentGroupDTOList = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.teacherDTOList = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.locationDTOList = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    /*
    herlaad alle listviews
     */
    public void refreshListViews(){
        refreshListView(dataAccessProvider.getDataAccessContext().getStudentDAO(), studentGroupDTOList);
        refreshListView(dataAccessProvider.getDataAccessContext().getTeacherDAO(), teacherDTOList);
        refreshListView(dataAccessProvider.getDataAccessContext().getLocationDAO(), locationDTOList);
    }

    /*
    Maak alle listviews leeg
     */
    public void clearListViews(){
        studentGroupDTOList.clear();
        teacherDTOList.clear();
        locationDTOList.clear();
    }

    /*
    Haal data uit databank via gegeven DAO en vul listview daarmee op.
     */
    private <T> void refreshListView(SimpleDAO<T> simpleDAO, ListProperty<T> listProperty) {
        listProperty.setAll(simpleDAO.getAllEntries());
    }

    /*
    Getters for the ListProperties
     */
    public ListProperty<StudentGroupDTO> studentGroupDTOListProperty() {
        return studentGroupDTOList;
    }
    public ListProperty<TeacherDTO> teacherDTOListProperty() {
        return teacherDTOList;
    }
    public ListProperty<LocationDTO> locationDTOListProperty() {
        return locationDTOList;
    }
}

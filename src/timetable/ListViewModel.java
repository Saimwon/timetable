/*
Simon Van Braeckel
 */

package timetable;

import datatransferobjects.LocationDTO;
import datatransferobjects.StudentGroupDTO;
import datatransferobjects.TeacherDTO;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;

public class ListViewModel {
    /*
    Representations of listviewcontent.
     */
    private ListProperty<StudentGroupDTO> studentGroupDTOList;
    private ListProperty<TeacherDTO> teacherDTOList;
    private ListProperty<LocationDTO> locationDTOList;

    public ListViewModel() {
        this.studentGroupDTOList = new SimpleListProperty<>();
        this.teacherDTOList = new SimpleListProperty<>();
        this.locationDTOList = new SimpleListProperty<>();
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

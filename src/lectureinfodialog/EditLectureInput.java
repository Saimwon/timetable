/*
Simon Van Braeckel
 */

package lectureinfodialog;

import datatransferobjects.LocationDTO;
import datatransferobjects.SimpleDTO;
import datatransferobjects.StudentGroupDTO;
import datatransferobjects.TeacherDTO;
import guielements.LectureRepresentation;

import java.util.List;

public class EditLectureInput extends LectureInput {
    public EditLectureInput(List<StudentGroupDTO> studentgroups, List<TeacherDTO> teachers, List<LocationDTO> locations,
                            List<String> startHours, LectureRepresentation source){
        super(studentgroups, teachers, locations, startHours, new EditLectureInputController(source));
        this.setTitle("Edit Lecture");
    }
}

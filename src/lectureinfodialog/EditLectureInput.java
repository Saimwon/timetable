/*
Simon Van Braeckel
 */

package lectureinfodialog;

import datatransferobjects.SimpleDTO;
import guielements.LectureRepresentation;

import java.util.List;

public class EditLectureInput extends LectureInput {
    public EditLectureInput(List<SimpleDTO> studentgroups, List<SimpleDTO> teachers, List<SimpleDTO> locations,
                            List<String> startHours, LectureRepresentation source){
        super(studentgroups, teachers, locations, startHours, new EditLectureInputController(source));
        this.setTitle("Edit Lecture");
    }
}

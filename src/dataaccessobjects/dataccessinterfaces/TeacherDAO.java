/*
Van Braeckel Simon
 */

package dataaccessobjects.dataccessinterfaces;
/*
Interface waarmee alle interactie met de teacher tabel in de DB gebeurt
 */

import datatransferobjects.TeacherDTO;

import java.util.List;

public interface TeacherDAO extends SimpleDAO {
    public List<TeacherDTO> getTeachers();
    public TeacherDTO getTeacherByID(int teacher_id);
    public List<TeacherDTO> getTeachersByName(String name);
}

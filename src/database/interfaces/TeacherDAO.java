/*
Van Braeckel Simon
 */

package database.interfaces;
/*
Interface waarmee alle interactie met de teacher tabel in de DB gebeurt
 */

import database.DataTransferObjects.TeacherDTO;

import java.util.List;

public interface TeacherDAO extends SimpleDAO {
    public List<TeacherDTO> getTeachers();
    public TeacherDTO getTeacherByID(int teacher_id);
    public List<TeacherDTO> getTeachersByName(String name);
}

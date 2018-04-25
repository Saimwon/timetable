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
    public List<TeacherDTO> getAllEntries();
    public TeacherDTO getEntryById(int teacher_id);
    public List<TeacherDTO> getEntryByName(String name);
}

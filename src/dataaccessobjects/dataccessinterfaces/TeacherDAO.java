/*
Van Braeckel Simon
 */

package dataaccessobjects.dataccessinterfaces;
/*
Interface waarmee alle interactie met de teacher tabel in de DB gebeurt
 */

import datatransferobjects.TeacherDTO;

public interface TeacherDAO extends SimpleDAO<TeacherDTO> {
    TeacherDTO getEntryById(int teacher_id);
}

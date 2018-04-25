/*
Van Braeckel Simon
 */

package dataaccessobjects.dataccessinterfaces;

import datatransferobjects.StudentGroupDTO;

import java.util.List;

/*
Interface waarmee alle interactie met de students tabel in de DB gebeurt
 */
public interface StudentGroupDAO extends SimpleDAO<StudentGroupDTO> {
    public List<StudentGroupDTO> getAllEntries();
    public List<StudentGroupDTO> getEntryByName(String name);
}

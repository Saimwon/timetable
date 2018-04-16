/*
Van Braeckel Simon
 */

package database.interfaces;

import database.DTO.StudentGroupDTO;

import java.util.List;

/*
Interface waarmee alle interactie met de students tabel in de DB gebeurt
 */
public interface StudentGroupDAO extends SimpleDAO{
    public List<StudentGroupDTO> getStudentGroups();
    public List<StudentGroupDTO> getStudentGroupsByName(String name);
}

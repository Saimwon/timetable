/*
Van Braeckel Simon
 */

package dataaccessobjects.dataccessinterfaces;
/*
Interface waarmee alle interactie met de lecture tabel in de DB gebeurt
 */


import datatransferobjects.LectureDTO;

import java.util.List;

public interface LectureDAO {
    public List<LectureDTO> getLecturesFromColumnById(String columnname, int id);
    public boolean addEntry(LectureDTO lectureDTO);
    public boolean deleteEntry(LectureDTO lectureDTO);
    public boolean lectureExists(LectureDTO lectureDTO);
}

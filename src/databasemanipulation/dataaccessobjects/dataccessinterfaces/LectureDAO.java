/*
Van Braeckel Simon
 */

package databasemanipulation.dataaccessobjects.dataccessinterfaces;
/*
Interface waarmee alle interactie met de lecture tabel in de DB gebeurt
 */


import datatransferobjects.LectureDTO;

import java.util.List;

public interface LectureDAO {
    List<LectureDTO> getLecturesFromColumnById(String columnname, int id);
    boolean addEntryIfNotExists(LectureDTO lectureDTO);
    boolean deleteEntry(LectureDTO lectureDTO);
    boolean lectureExists(LectureDTO lectureDTO);
    boolean tableExists();
}

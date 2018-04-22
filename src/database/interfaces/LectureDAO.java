/*
Van Braeckel Simon
 */

package database.interfaces;
/*
Interface waarmee alle interactie met de lecture tabel in de DB gebeurt
 */


import database.DataTransferObjects.LectureDTO;

import java.util.List;

public interface LectureDAO {
    public List<LectureDTO> getLecturesFromColumnById(String columnname, int id);
    public boolean addEntry(LectureDTO lectureDTO);
    public boolean deleteEntry(LectureDTO lectureDTO);
    public boolean lectureExists(LectureDTO lectureDTO);
}

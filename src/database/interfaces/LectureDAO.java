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
    public boolean addEntry(int students_id, int teacher_id, int location_id, String courseName, int day, int first_block, int duration);
}

package database.interfaces;
/*
Interface waarmee alle interactie met de teacher tabel in de DB gebeurt
 */

import database.DTO.TeacherDTO;

import java.util.List;

public interface TeacherDAO {
    public List<TeacherDTO> getTeachers();
    public TeacherDTO getTeacherByID(int teacher_id);
    public List<TeacherDTO> getTeachersByName(String name);
}

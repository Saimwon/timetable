package database.interfaces;
/*
Interface waarmee alle interactie met de lecture tabel in de DB gebeurt
 */


import database.DTO.LectureDTO;

import java.util.List;

public interface LectureDAO {
    public List<LectureDTO> getLecturesFromColumnById(String columnname, int id);
}

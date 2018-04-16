/*
Van Braeckel Simon
 */

package database.interfaces.implementations;

import database.DTO.LectureDTO;
import database.interfaces.LectureDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteLectureDAO implements LectureDAO{
    private Connection conn;

    public SQLiteLectureDAO(Connection conn){
        this.conn = conn;
    }

    @Override
    public List<LectureDTO> getLecturesFromColumnById(String columnName, int id){
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM lecture WHERE " + columnName + " = ?")){
//            statement.setString(1, columnName);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<LectureDTO> verwerkResultaat(ResultSet resultSet){
        List<LectureDTO> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                LectureDTO lecture = new LectureDTO(resultSet.getInt("students_id"), resultSet.getInt("teacher_id"), resultSet.getInt("location_id"), resultSet.getString("course"), resultSet.getInt("day"), resultSet.getInt("first_block"), resultSet.getInt("duration"));
                result.add(lecture);
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }

//    public List<LectureDTO> getSpecificLecture(int students_id, int teacher_id, int location_id, String courseName, int day, int first_block, int duration){
//        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM lecture WHERE students_id = ? and teacher_id = ? and location_id = ? and course like ? and day = ? and first_block = ? and duration = ?")){
//            statement.setInt(1, students_id);
//            statement.setInt(2, teacher_id);
//            statement.setInt(3, location_id);
//            statement.setString(4, courseName);
//            statement.setInt(5, day);
//            ResultSet resultSet = statement.executeQuery();
//            return verwerkResultaat(resultSet);
//        } catch (SQLException e){
//            e.printStackTrace();
//            return null;
//        }
//    }

    @Override
    public boolean addEntry(int students_id, int teacher_id, int location_id, String courseName, int day, int first_block, int duration){
        //het checken of de lecture al bestaat doen we niet want dat is niet echt nodig.
        //we krijgen wss een error als we dupes toevoegen


        //Voeg nieuwe entry toe aan DB
        try (PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO lecture values(?, ?, ?, ?, ?, ?, ?)"
        )) {
            statement.setInt(1, students_id);
            statement.setInt(2, teacher_id);
            statement.setInt(3, location_id);
            statement.setString(4, courseName);
            statement.setInt(5, day);
            statement.setInt(6, first_block);
            statement.setInt(7, duration);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("could not add lecture...");
            return false;
        }
        return true;
    }

}

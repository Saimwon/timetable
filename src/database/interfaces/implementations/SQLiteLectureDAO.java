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
}

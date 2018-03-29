package database.interfaces.implementations;

import database.DTO.StudentGroupDTO;
import database.interfaces.StudentGroupDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteStudentGroupDAO implements StudentGroupDAO{
    private Connection conn;
    public SQLiteStudentGroupDAO(Connection conn){
        this.conn = conn;
    }

    public List<StudentGroupDTO> getStudentGroups(){
        try (PreparedStatement statement = conn.prepareStatement("select * from students order by name")){
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<StudentGroupDTO> getStudentGroupsByName(String name){
        try (PreparedStatement statement = conn.prepareStatement("select * from students where name = ?")){
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<StudentGroupDTO> verwerkResultaat(ResultSet resultSet){
        List<StudentGroupDTO> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                result.add(new StudentGroupDTO(resultSet.getInt("id"), resultSet.getString("name")));
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }
}

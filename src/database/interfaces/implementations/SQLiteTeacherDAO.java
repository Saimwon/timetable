/*
Van Braeckel Simon
 */

package database.interfaces.implementations;

import database.DTO.StudentGroupDTO;
import database.DTO.TeacherDTO;
import database.interfaces.TeacherDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteTeacherDAO implements TeacherDAO{
    private Connection conn;

    public SQLiteTeacherDAO(Connection conn){
        this.conn = conn;
    }

    public TeacherDTO getTeacherByID(int teacher_id){
        try (PreparedStatement statement = conn.prepareStatement("select * from teacher where id = ? order by name")){
            statement.setInt(1, teacher_id);
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet).get(0);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<TeacherDTO> getTeachersByName(String name){
        try (PreparedStatement statement = conn.prepareStatement("select * from teacher where name = ?")){
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<TeacherDTO> getTeachers(){
        try (PreparedStatement statement = conn.prepareStatement("select * from teacher order by name")){
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<TeacherDTO> verwerkResultaat(ResultSet resultSet){
        List<TeacherDTO> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                result.add(new TeacherDTO(resultSet.getInt("id"), resultSet.getString("name")));
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }
}

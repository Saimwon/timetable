/*
Van Braeckel Simon
 */

package dataaccessobjects;

import datatransferobjects.StudentGroupDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteStudentGroupDAO implements StudentGroupDAO {
    private Connection conn;
    private String tablename = "students";

    public SQLiteStudentGroupDAO(Connection conn){
        this.conn = conn;
    }

    public List<StudentGroupDTO> getAllEntries(){
        try (PreparedStatement statement = conn.prepareStatement("select * from " + tablename + " order by name COLLATE NOCASE")){
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<StudentGroupDTO> getEntryByName(String name){
        try (PreparedStatement statement = conn.prepareStatement("select * from " + tablename + " where name = ?")){
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

    @Override
    public boolean addEntry(String name){
        //Voeg nieuwe entry toe aan DB
        try (PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO " + tablename + "(name) VALUES (?)"
        )) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("could not add entry");
            return false;
        }
        return true;
    }

    @Override
    public boolean renameEntry(int id, String newName){
        //Voeg nieuwe entry toe aan DB
        try (PreparedStatement statement = conn.prepareStatement(
                "UPDATE " + tablename + " SET name = ? WHERE id = ?")) {
            statement.setString(1, newName);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Failed to rename entry");
            return false;
        }
        return true;
    }
}

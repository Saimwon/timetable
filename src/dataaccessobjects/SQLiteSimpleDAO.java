/*
Simon Van Braeckel
 */
package dataaccessobjects;

import dataaccessobjects.dataccessinterfaces.SimpleDAO;
import datatransferobjects.LocationDTO;
import datatransferobjects.SimpleeDTO;
import datatransferobjects.StudentGroupDTO;
import datatransferobjects.TeacherDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class SQLiteSimpleDAO<T extends SimpleeDTO> implements SimpleDAO<T> {
    private Connection conn;
    private String tablename;

    public SQLiteSimpleDAO(Connection conn, String tablename) {
        this.conn = conn;
        this.tablename = tablename;
    }

    @Override
    public List<T> getAllEntries() {
        try (PreparedStatement statement = conn.prepareStatement("select * from " + tablename + " order by name COLLATE NOCASE")) {
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> getEntryByName(String name) {
        try (PreparedStatement statement = conn.prepareStatement("select * from " + tablename + " where name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<T> verwerkResultaat(ResultSet resultSet) {
        HashMap<String, Supplier<T>> dataTransferObjects = new HashMap<>();

        dataTransferObjects.put("teacher", () -> (T) new TeacherDTO()); //TODO schrijf dit op een manier waar je niet moet casten.
        dataTransferObjects.put("location", () -> (T) new LocationDTO());
        dataTransferObjects.put("students", () -> (T) new StudentGroupDTO());

        List<T> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                T dataTransferObject = dataTransferObjects.get(tablename).get();
                dataTransferObject.setId(resultSet.getInt("id"));
                dataTransferObject.setName(resultSet.getString("name"));

                result.add(dataTransferObject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    @Override
    public boolean addEntry(String name) {
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
    public boolean renameEntry(int id, String newName) {
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

    public T getEntryById(int teacher_id) {
        try (PreparedStatement statement = conn.prepareStatement("select * from " + tablename + " where id = ? order by name")) {
            statement.setInt(1, teacher_id);
            ResultSet resultSet = statement.executeQuery();
            List<T> teachers = verwerkResultaat(resultSet);
            if (teachers != null && teachers.size() != 0) {
                return teachers.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

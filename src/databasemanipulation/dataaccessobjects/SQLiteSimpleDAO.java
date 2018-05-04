/*
Simon Van Braeckel
 */
package databasemanipulation.dataaccessobjects;

import databasemanipulation.dataaccessobjects.dataccessinterfaces.SimpleDAO;
import datatransferobjects.SimpleDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SQLiteSimpleDAO<T extends SimpleDTO> implements SimpleDAO<T> {
    private Connection conn;
    private String tablename;
    private Supplier<T> supplier;

    public SQLiteSimpleDAO(Connection conn, String tablename, Supplier<T> supplier) {
        this.conn = conn;
        this.tablename = tablename;
        this.supplier = supplier;
    }

    /*
    Vraag alle entries op.
     */
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

    /*
    Vraag entries op met gegeven naam.
     */
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

    /*
    Zet resultset om in lijst van DTO's
     */
    private List<T> verwerkResultaat(ResultSet resultSet) {
        List<T> result = new ArrayList<>();

        try {
            while (resultSet.next()) {
                T dataTransferObject = supplier.get();
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

    /*
    Voeg een entry toe aan de databank
     */
    @Override
    public boolean addEntry(String name) {
        //Voeg nieuwe entry toe aan DB
        try (PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO " + tablename + "(name) VALUES (?)"
        )) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Failed to add entry: " + name);
            return false;
        }
        return true;
    }

    /*
    Geef entry met id naam newname
     */
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

    /*
    Vraag DTO op aan de hand van naam. Wordt normaal enkel gebruikt om
    in een lecturerepresentation de juiste prof te kunnen steken.
     */
    public T getEntryById(int id) {
        try (PreparedStatement statement = conn.prepareStatement("select * from " + tablename + " where id = ? order by name")) {
            statement.setInt(1, id);
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

/*
Van Braeckel Simon
 */

package database.interfaces.implementations;

import database.DTO.LocationDTO;
import database.interfaces.LocationDAO;
import database.interfaces.SimpleDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteLocationDAO implements LocationDAO {
    private Connection conn;
    private String tablename = "location";

    public SQLiteLocationDAO(Connection conn){
        this.conn = conn;
    }

    @Override
    public List<LocationDTO> getLocations() {
        try (PreparedStatement statement = conn.prepareStatement("select * from " + tablename + " order by name COLLATE NOCASE")){
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LocationDTO> getLocationsByName(String name){
        try (PreparedStatement statement = conn.prepareStatement("select * from " + tablename + " where name = ?")){
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<LocationDTO> verwerkResultaat(ResultSet resultSet){
        List<LocationDTO> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                result.add(new LocationDTO(resultSet.getInt("id"), resultSet.getString("name")));
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }

    @Override
    public boolean addEntry(String name){
        //Check of er al iemand is met die naam, zo ja -> error
        if (name.isEmpty() || ! getLocationsByName(name).isEmpty()){
            return false;
        }


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

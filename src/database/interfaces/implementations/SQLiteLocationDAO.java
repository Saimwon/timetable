/*
Van Braeckel Simon
 */

package database.interfaces.implementations;

import database.DTO.LocationDTO;
import database.DTO.TeacherDTO;
import database.interfaces.LocationDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteLocationDAO implements LocationDAO {
    private Connection conn;

    public SQLiteLocationDAO(Connection conn){
        this.conn = conn;
    }

    @Override
    public List<LocationDTO> getLocations() {
        try (PreparedStatement statement = conn.prepareStatement("select * from location order by name")){
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LocationDTO> getLocationsByName(String name){
        try (PreparedStatement statement = conn.prepareStatement("select * from location where name = ?")){
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
}

/*
Van Braeckel Simon
 */

package databasemanipulation.dataaccessobjects;

import databasemanipulation.dataaccessobjects.dataccessinterfaces.PeriodDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLitePeriodDAO implements PeriodDAO {
    private final Connection conn;

    public SQLitePeriodDAO(Connection conn){
        this.conn = conn;
    }

    @Override
    public List<String> getStartTimes() {
        try (PreparedStatement statement = conn.prepareStatement("select hour, minute from period order by hour, minute")){
            ResultSet resultSet = statement.executeQuery();

            return verwerkResultaat(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private List<String> verwerkResultaat(ResultSet resultSet) {
        List<String> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String hour = resultSet.getString("hour");
                String minute = String.format("%2s", resultSet.getString("minute")).replace(' ', '0');
                result.add(hour + ":" + minute);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}

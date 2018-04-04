/*
Van Braeckel Simon
 */

package database.interfaces.implementations;

import database.interfaces.DataAccessContext;
import database.interfaces.DataAccessProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLiteDataAccessProvider implements DataAccessProvider{
    private static String dbConnectionString = "jdbc:sqlite:src/timetable/lectures.db";

    public Connection makeConnection(){
        try {
            Connection connection = DriverManager.getConnection(dbConnectionString);
            return connection;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public DataAccessContext getDataAccessContext(){
        return new SQLiteDataAccessContext(makeConnection());
    }
}

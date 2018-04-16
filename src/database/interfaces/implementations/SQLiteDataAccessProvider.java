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
    private String dbConnectionString;

    public SQLiteDataAccessProvider() {
        dbConnectionString = "jdbc:sqlite::resource:timetable/lectures.db";
    }

    public Connection makeConnection(){
        try {
            Connection connection = DriverManager.getConnection(dbConnectionString);
            return connection;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void setDbConnectionString(String path){
        this.dbConnectionString = "jdbc:sqlite:" + path;
    }

    public String getDbConnectionString(){
        return dbConnectionString;
    }

    public DataAccessContext getDataAccessContext(){
        return new SQLiteDataAccessContext(makeConnection());
    }
}

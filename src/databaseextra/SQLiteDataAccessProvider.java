/*
Van Braeckel Simon
 */

package databaseextra;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteDataAccessProvider implements DataAccessProvider{
    private String dbConnectionString;

    public SQLiteDataAccessProvider() {
        dbConnectionString = "";//"jdbc:sqlite::resource:timetable/lectures.db";
    }

    private Connection makeConnection(){
        try {
            return DriverManager.getConnection(dbConnectionString);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void setDbConnectionString(String path){
        this.dbConnectionString = "jdbc:sqlite:" + path;
    }

    public String getDbConnectionString(){
        return this.dbConnectionString;
    }

    public DataAccessContext getDataAccessContext(){
        return new SQLiteDataAccessContext(makeConnection());
    }
}
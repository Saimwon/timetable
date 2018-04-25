/*
Van Braeckel Simon
 */

package databaseextra;

import java.sql.Connection;
import java.sql.DriverManager;

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

    public void makeNewDatabase(String path){
        this.setDbConnectionString(path);
        makeDatabase();
    }

    private void makeDatabase(){

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

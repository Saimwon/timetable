/*
Van Braeckel Simon
 */

package databaseextra;

import dataaccessobjects.*;
import dataaccessobjects.dataccessinterfaces.*;
import databasedefinition.SQLiteDatabaseDefiner;
import databasedefinition.DatabaseDefiner;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteDataAccessContext implements DataAccessContext{
    private Connection conn;
    public SQLiteDataAccessContext(Connection conn){
        this.conn = conn;
    }

    @Override
    public StudentGroupDAO getStudentDAO(){
        return new SQLiteStudentGroupDAO(conn);
    }

    @Override
    public TeacherDAO getTeacherDAO(){
        return new SQLiteTeacherDAO(conn);
    }

    @Override
    public LocationDAO getLocationDAO(){
        return new SQLiteLocationDAO(conn);
    }

    @Override
    public LectureDAO getLectureDAO(){
        return new SQLiteLectureDAO(conn);
    }

    @Override
    public PeriodDAO getPeriodDAO() {
        return new SQLitePeriodDAO(conn);
    }

    @Override
    public DatabaseDefiner getDatabaseDefiner(){
        return new SQLiteDatabaseDefiner(conn);
    }

    @Override
    public void close(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
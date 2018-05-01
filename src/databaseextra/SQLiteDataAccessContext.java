/*
Van Braeckel Simon
 */

package databaseextra;

import dataaccessobjects.*;
import dataaccessobjects.dataccessinterfaces.*;
import databasedefinition.SQLiteDatabaseDefiner;
import databasedefinition.DatabaseDefiner;
import datatransferobjects.LocationDTO;
import datatransferobjects.StudentGroupDTO;
import datatransferobjects.TeacherDTO;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteDataAccessContext implements DataAccessContext{
    private final Connection conn;
    public SQLiteDataAccessContext(Connection conn){
        this.conn = conn;
    }

    @Override
    public SQLiteSimpleDAO<StudentGroupDTO> getStudentDAO(){
        return new SQLiteSimpleDAO<>(conn, "students");
    }

    @Override
    public SQLiteSimpleDAO<TeacherDTO> getTeacherDAO(){
        return new SQLiteSimpleDAO<>(conn, "teacher");
    }

    @Override
    public SQLiteSimpleDAO<LocationDTO> getLocationDAO(){
        return new SQLiteSimpleDAO<>(conn, "location");
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

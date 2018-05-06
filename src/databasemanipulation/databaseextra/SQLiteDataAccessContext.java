/*
Van Braeckel Simon
 */

package databasemanipulation.databaseextra;

import databasemanipulation.dataaccessobjects.*;
import databasemanipulation.dataaccessobjects.dataccessinterfaces.*;
import databasemanipulation.databasedefinition.DatabaseDefiner;
import databasemanipulation.databasedefinition.SQLiteDatabaseDefiner;
import datatransferobjects.LocationDTO;
import datatransferobjects.SimpleDTO;
import datatransferobjects.StudentGroupDTO;
import datatransferobjects.TeacherDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public class SQLiteDataAccessContext implements DataAccessContext {
    private final Connection conn;
    SQLiteDataAccessContext(Connection conn){
        this.conn = conn;
    }

    @Override
    public SQLiteSimpleDAO<StudentGroupDTO> getStudentDAO(){
        return new SQLiteSimpleDAO<>(conn, "students", StudentGroupDTO::new);
    }

    @Override
    public SQLiteSimpleDAO<TeacherDTO> getTeacherDAO(){
        return new SQLiteSimpleDAO<>(conn, "teacher", TeacherDTO::new);
    }

    @Override
    public SQLiteSimpleDAO<LocationDTO> getLocationDAO(){
        return new SQLiteSimpleDAO<>(conn, "location", LocationDTO::new);
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

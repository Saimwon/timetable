/*
Van Braeckel Simon
 */

package databaseextra;

import dataaccessobjects.dataccessinterfaces.*;
import databasedefinition.DatabaseDefiner;

public interface DataAccessContext extends AutoCloseable {
    public StudentGroupDAO getStudentDAO();
    public TeacherDAO getTeacherDAO();
    public LocationDAO getLocationDAO();
    public LectureDAO getLectureDAO();
    public PeriodDAO getPeriodDAO();
    public DatabaseDefiner getDatabaseDefiner();

    @Override
    public void close();
}

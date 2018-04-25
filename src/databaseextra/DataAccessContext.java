/*
Van Braeckel Simon
 */

package databaseextra;

import dataaccessobjects.dataccessinterfaces.*;
import databasedefinition.DatabaseDefiner;
import datatransferobjects.TeacherDTO;

public interface DataAccessContext extends AutoCloseable {
    public StudentGroupDAO getStudentDAO();
    public SimpleDAO<TeacherDTO> getTeacherDAO();
    public LocationDAO getLocationDAO();
    public LectureDAO getLectureDAO();
    public PeriodDAO getPeriodDAO();
    public DatabaseDefiner getDatabaseDefiner();

    @Override
    public void close();
}

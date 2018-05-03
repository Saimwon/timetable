/*
Van Braeckel Simon
 */

package databaseextra;

import dataaccessobjects.dataccessinterfaces.*;
import databasedefinition.DatabaseDefiner;
import datatransferobjects.LocationDTO;
import datatransferobjects.StudentGroupDTO;
import datatransferobjects.TeacherDTO;

public interface DataAccessContext extends AutoCloseable {
    SimpleDAO<StudentGroupDTO> getStudentDAO();
    SimpleDAO<TeacherDTO> getTeacherDAO();
    SimpleDAO<LocationDTO> getLocationDAO();
    LectureDAO getLectureDAO();
    PeriodDAO getPeriodDAO();
    DatabaseDefiner getDatabaseDefiner();

    @Override
    void close();
}

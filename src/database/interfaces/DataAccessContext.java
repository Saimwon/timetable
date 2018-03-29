package database.interfaces;

public interface DataAccessContext extends AutoCloseable {
    public StudentGroupDAO getStudentDAO();
    public TeacherDAO getTeacherDAO();
    public LocationDAO getLocationDAO();
    public LectureDAO getLectureDAO();
    public PeriodDAO getPeriodDAO();

    @Override
    public void close();
}

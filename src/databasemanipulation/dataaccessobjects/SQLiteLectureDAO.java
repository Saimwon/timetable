/*
Van Braeckel Simon
 */

package databasemanipulation.dataaccessobjects;

import databasemanipulation.dataaccessobjects.dataccessinterfaces.LectureDAO;
import datatransferobjects.LectureDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteLectureDAO implements LectureDAO {
    private final Connection conn;

    public SQLiteLectureDAO(Connection conn){
        this.conn = conn;
    }

    @Override
    public List<LectureDTO> getLecturesFromColumnById(String columnName, int id){
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM lecture WHERE " + columnName + " = ?")){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /*
    Methode die een resultset neemt en omzet in een lijst van lectureDTO's.
     */
    private List<LectureDTO> verwerkResultaat(ResultSet resultSet){
        List<LectureDTO> result = new ArrayList<>();

        try {
            while (resultSet.next()) {
                LectureDTO lecture = new LectureDTO(resultSet.getInt("students_id"),
                        resultSet.getInt("teacher_id"),
                        resultSet.getInt("location_id"),
                        resultSet.getString("course"),
                        resultSet.getInt("day"),
                        resultSet.getInt("first_block"),
                        resultSet.getInt("duration"));
                result.add(lecture);
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /*
    Methode die checkt of een gegeven lecture al bestaat in de databank.
     */
    @Override
    public boolean lectureExists(LectureDTO lectureDTO){
        try (PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM lecture WHERE students_id = ? AND teacher_id = ? AND location_id = ? AND course = ? AND day = ? AND first_block = ? and duration = ?"
        )) {
            setStatementData(statement, lectureDTO);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException ex) {
            System.out.println("Iets gaat mis in het checken of deze lecture al bestaat.");
            ex.printStackTrace();
            return false;
        }
    }

    /*
    Methode die een lecture toevoegt aan de databank als die er nog niet in zit.
     */
    @Override
    public boolean addEntryIfNotExists(LectureDTO lectureDTO){
        //check of lecture al bestaat
        if (lectureExists(lectureDTO)){
            return false;
        }

        //Voeg nieuwe entry toe aan DB
        try (PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO lecture values(?, ?, ?, ?, ?, ?, ?)"
        )) {
            setStatementData(statement, lectureDTO);

            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Failed to add the lecture...");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteEntry(LectureDTO lectureDTO){
        try (PreparedStatement statement = conn.prepareStatement("DELETE FROM lecture WHERE students_id = ? AND teacher_id = ? AND location_id = ? AND course LIKE ? AND day = ? AND first_block = ? AND duration = ?")){
            setStatementData(statement, lectureDTO);

            statement.executeUpdate();
            return true;
        } catch (SQLException e){
            System.out.println("Error in deleting lecture.");
            e.printStackTrace();
            return false;
        }
    }

    /*
    Methode die checkt of de tabel period bestaat.
     */
    @Override
    public boolean tableExists(){
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM lecture")){
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    private void setStatementData(PreparedStatement statement, LectureDTO lectureDTO) throws SQLException {
        statement.setInt(1, lectureDTO.getStudent_id());
        statement.setInt(2, lectureDTO.getTeacher_id());
        statement.setInt(3, lectureDTO.getLocation_id());
        statement.setString(4, lectureDTO.getCourse());
        statement.setInt(5, lectureDTO.getDay());
        statement.setInt(6, lectureDTO.getFirst_block());
        statement.setInt(7, lectureDTO.getDuration());
    }
}

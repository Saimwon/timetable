/*
Simon Van Braeckel
 */
package databaseextra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
Klasse die instaat voor de databankdefinitie wanneer er een nieuwe databank geopend wordt in de applicatie.
 */
public class SQLiteDatabaseDefiner implements DatabaseDefiner {
    private static String[] tabellen = {"students", "location", "teacher"};
    private static String[] createQuerys = {"create table period   ('id' integer primary key, 'hour', 'minute')",
            "create table students ('id' integer primary key, 'name')",
            "create table location ('id' integer primary key, 'name')",
            "create table teacher('id' integer primary key, 'name')",

            "create table lecture  ('students_id', 'teacher_id', 'location_id', 'course', 'day', 'first_block', 'duration')"};
    private static String[] dropQuerys = {"drop table period",
            "drop table students",
            "drop table location",
            "drop table teacher",
            "drop table lecture"};
    private Connection conn;

    public SQLiteDatabaseDefiner(Connection conn){
        this.conn = conn;
    }

    private void execute(String[] querys) throws SQLException {
        for (String query : querys) {
         execute(query);
        }
    }

    private void execute(String query) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            int ding = statement.executeUpdate();
        }
    }

    public void define(List<Integer[]> startHours){
        try {
            execute(dropQuerys);
        } catch (SQLException ignored){}

        try {
            execute(createQuerys);
        } catch (SQLException e){
            crash(e);
        }

        for (Integer[] period : startHours){
            try {
                execute("INSERT INTO period (hour, minute) VALUES (" + period[0] + ", " + period[1] + ")");
            } catch (SQLException e){
                crash(e);
            }
        }
    }

    private void crash(Exception e){
        System.out.println("Something went wrong in creating the database.");
        e.printStackTrace();
        System.exit(1);
    }

    public void executeLine(String line) {
        for (String tabel : tabellen) {
            try (PreparedStatement statement = conn.prepareStatement(line)) {
                statement.setString(1, tabel);
                ResultSet resultSet = statement.executeQuery();
            } catch (SQLException e) {
                System.out.println();
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}

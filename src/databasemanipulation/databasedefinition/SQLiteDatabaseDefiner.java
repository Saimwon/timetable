/*
Simon Van Braeckel
 */
package databasemanipulation.databasedefinition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/*
Klasse die instaat voor de databankdefinitie wanneer er een nieuwe databank geopend wordt in de applicatie.
 */
public class SQLiteDatabaseDefiner implements DatabaseDefiner {
    private Connection conn;
    private static String dropTableFile = "droptablequeries.txt";
    private static String createTableFile = "createtablequeries.txt";

    public SQLiteDatabaseDefiner(Connection conn){
        this.conn = conn;
    }

    /*
    Voert 1 query uit op de databank. Vangt errors niet zelf op.
     */
    private void execute(String query) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            int ding = statement.executeUpdate();
        }
    }

    /*
    Methode die de databank aanmaakt wanneer er "create database" wordt gedaan.
     */
    public void define(List<Integer[]> startHours){
        try {
            executeDropQueries();
            executeCreateQueries();

            for (Integer[] period : startHours) {
                execute("INSERT INTO period (hour, minute) VALUES (" + period[0] + ", " + period[1] + ")");
            }
        } catch (SQLException | IOException e){
            crash(e);
        }
    }

    private void executeDropQueries() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(SQLiteDatabaseDefiner.class.getResourceAsStream(dropTableFile)));
        String line = bufferedReader.readLine();

        while (line != null){
            try {
                execute(line);
            } catch (SQLException ignored){}

            line = bufferedReader.readLine();
        }
    }

    private void executeCreateQueries() throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(SQLiteDatabaseDefiner.class.getResourceAsStream(createTableFile)));
        String line = bufferedReader.readLine();
        while (line != null) {
            execute(line);
            line = bufferedReader.readLine();
        }
    }

    /*
    Methode die het programma doet crashen.
     */
    private void crash(Exception e){
        System.out.println("Something went wrong in creating the database.");
        e.printStackTrace();
        System.exit(1);
    }
}

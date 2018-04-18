/*
Simon Van Braeckel
 */
package database.interfaces.implementations;

import database.interfaces.DatabaseDefiner;

import java.sql.Connection;
import java.util.List;

public class SQLiteDatabaseDefiner implements DatabaseDefiner {
    private Connection conn;

    public SQLiteDatabaseDefiner(Connection conn){
        this.conn = conn;
    }

    public void define(List<String> starturen){ // moet gesorteerd zijn

    }
}

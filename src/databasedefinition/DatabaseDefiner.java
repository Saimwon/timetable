/*
Simon Van Braeckel
 */
package databasedefinition;

import java.sql.Connection;
import java.util.List;

public interface DatabaseDefiner {
    public void define(List<Integer[]> startHours);
}

/*
Simon Van Braeckel
 */
package databasemanipulation.databasedefinition;

import java.util.List;

public interface DatabaseDefiner {
    void define(List<Integer[]> startHours);
}

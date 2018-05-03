/*
Simon Van Braeckel
 */
package databasedefinition;

import java.util.List;

public interface DatabaseDefiner {
    void define(List<Integer[]> startHours);
}

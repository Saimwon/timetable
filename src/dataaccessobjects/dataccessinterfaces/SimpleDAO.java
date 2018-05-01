/*
Simon Van Braeckel
 */
package dataaccessobjects.dataccessinterfaces;


import java.util.List;

//superclass for DataAccessObjects that manipulate tables with only an id column and a name column
public interface SimpleDAO<T> {
    List<T> getAllEntries();
    List<T> getEntryByName(String name);
    boolean addEntry(String name);
    boolean renameEntry(int id, String newName);
    T getEntryById(int id);
}

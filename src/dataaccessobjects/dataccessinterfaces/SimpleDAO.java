/*
Simon Van Braeckel
 */
package dataaccessobjects.dataccessinterfaces;


import datatransferobjects.SimpleDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//superclass for DataAccessObjects that manipulate tables with only an id column and a name column
public interface SimpleDAO<T> {
    public List<T> getAllEntries();
    public List<T> getEntryByName(String name);
    public T getEntryById(int id);
    public boolean addEntry(String name);
    public boolean renameEntry(int id, String newName);
}

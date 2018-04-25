/*
Simon Van Braeckel
 */
package dataaccessobjects.dataccessinterfaces;


//superclass for DataAccessObjects that manipulate tables with only an id column and a name column
public interface SimpleDAO {
    public boolean addEntry(String name);
    public boolean renameEntry(int id, String newName);
}

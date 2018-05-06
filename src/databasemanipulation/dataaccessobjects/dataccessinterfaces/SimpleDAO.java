/*
Simon Van Braeckel
 */
package databasemanipulation.dataaccessobjects.dataccessinterfaces;


import java.util.List;

//superclass for DataAccessObjects that manipulate tables with only an id column and a name column
public interface SimpleDAO<T> {
    List<T> getAllEntries();
    List<T> getEntryByName(String name);
    /*
    Methode die Location, Studentgroup of Teacher toevoegt. Geeft true of false terug afhankelijk van of er zich een error heef voorgedaan of niet.
     */
    boolean addEntry(String name);
    /*
    Methode die Location, Studentgroup of Teacher hernoemt. Geeft true of false terug afhankelijk van of er zich een error heef voorgedaan of niet.
     */
    boolean renameEntry(int id, String newName);
    T getEntryById(int id);
}

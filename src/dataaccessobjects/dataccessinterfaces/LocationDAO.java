/*
Van Braeckel Simon
 */

package dataaccessobjects.dataccessinterfaces;
/*
Interface waarmee alle interactie met de location tabel in de DB gebeurt
 */

import datatransferobjects.LocationDTO;

import java.util.List;

public interface LocationDAO extends SimpleDAO<LocationDTO> {
    List<LocationDTO> getAllEntries();
    List<LocationDTO> getEntryByName(String name);
}

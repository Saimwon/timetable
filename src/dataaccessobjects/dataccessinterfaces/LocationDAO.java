/*
Van Braeckel Simon
 */

package dataaccessobjects.dataccessinterfaces;
/*
Interface waarmee alle interactie met de location tabel in de DB gebeurt
 */

import datatransferobjects.LocationDTO;

import java.util.List;

public interface LocationDAO extends SimpleDAO{
    public List<LocationDTO> getLocations();
    public List<LocationDTO> getLocationsByName(String name);
}

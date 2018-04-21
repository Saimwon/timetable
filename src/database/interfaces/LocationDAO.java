/*
Van Braeckel Simon
 */

package database.interfaces;
/*
Interface waarmee alle interactie met de location tabel in de DB gebeurt
 */

import database.DataTransferObjects.LocationDTO;

import java.util.List;

public interface LocationDAO extends SimpleDAO{
    public List<LocationDTO> getLocations();
    public List<LocationDTO> getLocationsByName(String name);
}

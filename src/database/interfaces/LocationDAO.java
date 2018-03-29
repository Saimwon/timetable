package database.interfaces;
/*
Interface waarmee alle interactie met de location tabel in de DB gebeurt
 */

import database.DTO.LocationDTO;

import java.util.List;

public interface LocationDAO {
    public List<LocationDTO> getLocations();
    public List<LocationDTO> getLocationsByName(String name);
}

/*
Van Braeckel Simon
 */

package database.DTO;

public class LocationDTO extends DTO{
    private int id;
    private String name;

    public LocationDTO(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

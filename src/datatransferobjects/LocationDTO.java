/*
Van Braeckel Simon
 */

package datatransferobjects;

public class LocationDTO extends SimpleeDTO {
    public LocationDTO(int id, String name){
        super(id, name, "location");
    }

    public LocationDTO(){
        super("location");
    }
}

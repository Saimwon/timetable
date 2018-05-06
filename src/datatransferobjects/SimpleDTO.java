/*
Simon Van Braeckel
 */

package datatransferobjects;

public class SimpleDTO implements NameIdDTO {
    private int id;
    private String name;

    SimpleDTO(int id, String name){
        this.id = id;
        this.name = name;
    }

    SimpleDTO(){
        this.id = -1;
        this.name = "";
    }

    public void setName(String name){
        this.name = name;
    }

    public void setId(int id){
        this.id = id;
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

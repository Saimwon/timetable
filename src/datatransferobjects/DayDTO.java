/*
Simon Van Braeckel
 */

package datatransferobjects;

public class DayDTO implements NameIdDTO {
    private int Id;
    private String name;

    public DayDTO(int dayNr, String day) {
        this.Id = dayNr;
        this.name = day;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return this.name;
    }
}

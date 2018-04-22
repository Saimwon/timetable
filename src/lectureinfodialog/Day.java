/*
Simon Van Braeckel
 */

package lectureinfodialog;

public class Day implements NameIdDTO {
    private int Id;
    private String name;

    public Day(int dayNr, String day) {
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

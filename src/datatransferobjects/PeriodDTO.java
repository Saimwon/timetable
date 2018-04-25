/*
Simon Van Braeckel
 */

package datatransferobjects;

public class PeriodDTO implements NameIdDTO {
    private int Id;
    private String name;

    public PeriodDTO(int periodNr, String period) {
        this.Id = periodNr;
        this.name = period;
    }

    public int getId() {
        return Id;
    }

    public void setId(int dayNr) {
        this.Id = dayNr;
    }

    public String getName() {
        return name;
    }

    public void setName(String day) {
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}

/*
Simon Van Braeckel
 */

package datatransferobjects;

public class PeriodDTO implements NameIdDTO {
    private int Id;
    private String startTime;

    public PeriodDTO(int periodNr, String period) {
        this.Id = periodNr;
        this.startTime = period;
    }

    public int getId() {
        return Id;
    }

    public void setId(int dayNr) {
        this.Id = dayNr;
    }

    public String getName() {
        return startTime;
    }

    public void setName(String starttime) {
        this.startTime = starttime;
    }

    @Override
    public String toString(){
        return this.startTime;
    }
}

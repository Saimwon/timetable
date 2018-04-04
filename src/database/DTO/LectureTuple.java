/*
Van Braeckel Simon
 */

package database.DTO;

public class LectureTuple {
    private String courseName;
    private int day;
    private int hour;

    public LectureTuple(String courseName, int day, int hour){
        this.courseName = courseName;
        this.day = day;
        this.hour = hour;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }
}

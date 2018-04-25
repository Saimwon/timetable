/*
Van Braeckel Simon
 */

package datatransferobjects;

public class LectureDTO{

    private int student_id;
    private int teacher_id;
    private int location_id;
    private String course;
    private int day;
    private int first_block;
    private int duration;

    public LectureDTO(int student_id, int teacher_id, int location_id, String course, int day, int first_block, int duration){
        this.student_id = student_id;
        this.teacher_id = teacher_id;
        this.location_id = location_id;
        this.course = course;
        this.day = day;
        this.first_block = first_block;
        this.duration = duration;
    }

    public int getStudent_id() {
        return student_id;
    }

    public int getLocation_id() {
        return location_id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public String getCourse() {
        return course;
    }

    public int getDay() {
        return day;
    }

    public int getFirst_block() {
        return first_block;
    }

    public int getDuration() {
        return duration;
    }

    public boolean equals(LectureDTO o2){
        boolean student = this.student_id == o2.getStudent_id();
        boolean teacher = this.teacher_id == o2.getTeacher_id();
        boolean location = this.location_id == o2.getLocation_id();
        boolean coursename = this.course.equals(o2.getCourse());
        boolean day = this.day == o2.getDay();
        boolean period = this.first_block == o2.getFirst_block();
        boolean duration = this.duration == o2.getDuration();

        return student && teacher && location && coursename && day && period && duration;
    }
}

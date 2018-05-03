/*
Van Braeckel Simon
 */

package datatransferobjects;

public class TeacherDTO extends SimpleDTO {
    public TeacherDTO(int id, String name){
        super(id, name, "teacher");
    }

    public TeacherDTO(){
        super("teacher");
    }
}
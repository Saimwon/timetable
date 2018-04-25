/*
Van Braeckel Simon
 */

package datatransferobjects;

public class StudentGroupDTO extends SimpleeDTO {
    public StudentGroupDTO(int id, String name){
        super(id, name, "students");
    }

    public StudentGroupDTO(){
        super("students");
    }
}
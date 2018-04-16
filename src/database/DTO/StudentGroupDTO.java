/*
Van Braeckel Simon
 */

package database.DTO;

public class StudentGroupDTO implements SimpleDTO {
    private static String tableName = "students";
    private int id;
    private String name;

    public StudentGroupDTO(int id, String name){
        this.id = id;
        this.name = name;
    }


    public String getTableName() {
        return tableName;
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
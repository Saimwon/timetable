/*
Van Braeckel Simon
 */

package database.DataTransferObjects;

public class TeacherDTO implements SimpleDTO {
    private static String tableName = "teacher";
    private int id;
    private String name;

    public TeacherDTO(int id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
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

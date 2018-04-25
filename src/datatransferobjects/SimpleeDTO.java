/*
Simon Van Braeckel
 */

package datatransferobjects;

public class SimpleeDTO implements SimpleDTO {
    private String tableName;
    private int id;
    private String name;

    public SimpleeDTO(int id, String name, String tableName){
        this.id = id;
        this.name = name;
        this.tableName = tableName;
    }

    public SimpleeDTO(String tableName){
        this.id = -1;
        this.name = "";
        this.tableName = tableName;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setId(int id){
        this.id = id;
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

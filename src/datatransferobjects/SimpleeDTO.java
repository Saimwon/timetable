/*
Simon Van Braeckel
 */

package datatransferobjects;

public class SimpleeDTO implements SimpleDTO {
    private String tableName;
    private int id;
    private String name;

    SimpleeDTO(int id, String name, String tableName){
        this.id = id;
        this.name = name;
        this.tableName = tableName;
    }

    SimpleeDTO(String tableName){
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

    //equals methode toevoegen
    @Override
    public boolean equals(Object o2) {
        if (o2 == null) {
            return false;
        }
        if (!SimpleeDTO.class.isAssignableFrom(o2.getClass())) {
            return false;
        }
        final SimpleeDTO other = (SimpleeDTO) o2;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return (this.id != other.id);
    }
}

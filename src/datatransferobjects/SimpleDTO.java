/*
Van Braeckel Simon

Gemeenschappelijke interface voor de 3 tabellen in de DB die exact hetzelfde zijn behalve de naam.
 */

package datatransferobjects;

public interface SimpleDTO extends NameIdDTO{
    String getTableName();
}

/*
Van Braeckel Simon

Gemeenschappelijke interface voor de 3 tabellen in de DB die exact hetzelfde zijn behalve de naam.
 */

package database.DataTransferObjects;

import lectureinfodialog.NameIdDTO;

public interface SimpleDTO extends NameIdDTO{
    public String getTableName();
}

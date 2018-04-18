/*
Van Braeckel Simon
 */

package database.interfaces;

import java.sql.Connection;

public interface DataAccessProvider {
    public DataAccessContext getDataAccessContext();
    public void setDbConnectionString(String path);
    public String getDbConnectionString();
    public void makeNewDatabase(String path);
}

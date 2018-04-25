/*
Van Braeckel Simon
 */

package databaseextra;

public interface DataAccessProvider {
    public DataAccessContext getDataAccessContext();
    public void setDbConnectionString(String path);
    public String getDbConnectionString();
    public void makeNewDatabase(String path);
}

/*
Van Braeckel Simon
 */

package databasemanipulation.databaseextra;

public interface DataAccessProvider {
    DataAccessContext getDataAccessContext();
    void setDbConnectionString(String path);
    String getDbConnectionString();
}

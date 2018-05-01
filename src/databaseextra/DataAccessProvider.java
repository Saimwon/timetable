/*
Van Braeckel Simon
 */

package databaseextra;

public interface DataAccessProvider {
    DataAccessContext getDataAccessContext();
    void setDbConnectionString(String path);
}

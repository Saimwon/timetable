/*
Van Braeckel Simon
 */

package database.interfaces;

import java.sql.Connection;

public interface DataAccessProvider {
    public DataAccessContext getDataAccessContext();
}

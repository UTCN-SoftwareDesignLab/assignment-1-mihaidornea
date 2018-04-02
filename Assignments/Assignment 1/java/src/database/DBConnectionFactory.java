package database;

import static database.Constants.Schemas.PRODUCTION;

public class DBConnectionFactory {

    public JDBConnectionWrapper getConnectionWrapper(){
        return new JDBConnectionWrapper(PRODUCTION);
    }
}

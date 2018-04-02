package database;

import static database.Constants.Schemas.PRODUCTION;

public class DBConnectionFactory {

    public JDBConnectionWrapper getConnectionWrapper(boolean test){
        if (!test)
            return new JDBConnectionWrapper(PRODUCTION);
        else
            return new JDBConnectionWrapper("test_bank");
    }
}

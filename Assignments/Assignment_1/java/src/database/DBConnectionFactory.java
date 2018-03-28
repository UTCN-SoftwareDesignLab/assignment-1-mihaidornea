package database;

public class DBConnectionFactory {

    public JDBConnectionWrapper getConnectionWrapper(){
        return new JDBConnectionWrapper(Constants.Schemas.PRODUCTION);
    }
}

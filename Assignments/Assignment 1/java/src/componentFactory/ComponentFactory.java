package componentFactory;

import database.DBConnectionFactory;
import database.SQLTableCreationFactory;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.account.AccountService;
import service.client.ClientService;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;

import java.awt.desktop.UserSessionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static database.Constants.Tables.ORDERED_TABLES_FOR_CREATION;

public class ComponentFactory {

    private final AuthenticationService authenticationService;

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final AccountRepository accountRepository;

    private static ComponentFactory instance;

    public static ComponentFactory instance(){
        if (instance == null){
            instance = new ComponentFactory();
        }
        return instance;
    }

    private ComponentFactory() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(false).getConnection();
        /*SQLTableCreationFactory sqlTableCreationFactory = new SQLTableCreationFactory();
        for(String s : ORDERED_TABLES_FOR_CREATION){
            try {
                String sql = sqlTableCreationFactory.getCreateSQLForTable(s);
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }*/
        this.clientRepository = new ClientRepositoryMySQL(connection);
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceMySQL(this.userRepository, this.rightsRolesRepository);
        this.accountRepository = new AccountRepositoryMySQL(connection);

    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }


    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }

    public AccountRepository getAccountRepository () { return accountRepository; }

    public ClientRepository getClientRepository(){
        return clientRepository;
    }
}

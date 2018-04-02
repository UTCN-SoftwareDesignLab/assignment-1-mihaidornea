package repository.user;

import database.DBConnectionFactory;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.security.RightsRolesRepositoryMySQLTest;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;

import javax.naming.AuthenticationException;
import java.sql.Connection;

import static database.Constants.Roles.ADMINISTRATOR;

public class AuthenticationServiceTest {

    public static final String TEST_USERNAME = "mihaidornea";
    public static final String TEST_PASSWORD = "TestPass2word";
    private static UserRepository userRepository;
    private static RightsRolesRepository rightsRolesRepository;
    private static Connection connection;
    private static AuthenticationService authenticationService;

    @BeforeClass
    public static void setUp() {
        connection = new DBConnectionFactory().getConnectionWrapper().getConnection();
        rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        authenticationService = new AuthenticationServiceMySQL(userRepository, rightsRolesRepository);
    }

    @Before
    public void cleanUp() { userRepository.removeAll(); }

    @Test
    public void login() throws AuthenticationException{
        cleanUp();
        Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        User user = new UserBuilder().setUsername(TEST_USERNAME)
                .setPassword(TEST_PASSWORD)
                .addRole(role)
                .build();
       userRepository.save(user);
       Assert.assertFalse(authenticationService.login(user.getUsername(), user.getPassword()).hasErrors());
    }


    @Test
    public void getUserRole() throws AuthenticationException{
        cleanUp();
        Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        User user = new UserBuilder().setUsername(TEST_USERNAME)
                .setPassword(TEST_PASSWORD)
                .addRole(role)
                .build();
        userRepository.save(user);
        Assert.assertNotNull(authenticationService.getUserRole(TEST_USERNAME, TEST_PASSWORD));
    }

    @Test
    public void getUserId() throws AuthenticationException{
        cleanUp();
        Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        User user = new UserBuilder().setUsername(TEST_USERNAME)
                .setPassword(TEST_PASSWORD)
                .addRole(role)
                .build();
        userRepository.save(user);
        Assert.assertTrue(authenticationService.getUserId(TEST_USERNAME, TEST_PASSWORD) > 0L);
    }
}

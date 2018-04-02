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
import service.user.UserService;
import service.user.UserServiceImpl;

import javax.naming.AuthenticationException;
import java.sql.Connection;

import static database.Constants.Roles.ADMINISTRATOR;

public class UserServiceImplTest {

    public static final String TEST_USERNAME = "mihaidornea";
    public static final String TEST_PASSWORD = "TestPass2word";
    private static UserRepository userRepository;
    private static RightsRolesRepository rightsRolesRepository;
    private static Connection connection;
    private static UserService userService;

    @BeforeClass
    public static void setUp() {
        connection = new DBConnectionFactory().getConnectionWrapper().getConnection();
        rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        userService = new UserServiceImpl(userRepository, rightsRolesRepository);
    }

    @Before
    public void cleanUp() {
        userRepository.removeAll();
    }

    @Test
    public void findAll(){
        Assert.assertNotNull(userRepository.findAll());
    }

    @Test
    public void save(){
        cleanUp();
        Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        User user = new UserBuilder().setUsername(TEST_USERNAME)
                .setPassword(TEST_PASSWORD)
                .addRole(role)
                .build();
        Assert.assertFalse(userService.save(user).hasErrors());
    }

    @Test
    public void findByUsernameAndPassword() throws AuthenticationException {
        Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        User user = new UserBuilder().setUsername(TEST_USERNAME)
                .setPassword(TEST_PASSWORD)
                .addRole(role)
                .build();
        userService.save(user);
        Notification<User> notification = userService.findByUsername(TEST_USERNAME);
        Assert.assertNotNull(notification.getResult());
    }

    @Test
    public void findByUsername(){
        Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        User user = new UserBuilder().setUsername(TEST_USERNAME)
                .setPassword(TEST_PASSWORD)
                .addRole(role)
                .build();
        userService.save(user);
        Notification<User> notification = userService.findByUsername(user.getUsername());
        Assert.assertFalse(notification.hasErrors());
    }

    @Test
    public void update(){
        Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        User user = new UserBuilder().setUsername(TEST_USERNAME)
                .setPassword(TEST_PASSWORD)
                .addRole(role)
                .build();
        userService.save(user);
        user.setUsername("mihaidornea2");
        Assert.assertFalse(userService.update(user).hasErrors());
    }

    @Test
    public void removeAll(){
        userService.removeAll();
        Assert.assertTrue(userService.findAll().isEmpty());
    }

    @Test
    public void removeUser(){
        userService.removeAll();
        Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        User user = new UserBuilder().setUsername(TEST_USERNAME)
                .setPassword(TEST_PASSWORD)
                .addRole(role)
                .build();
        userService.save(user);

        User user1 = new UserBuilder().setUsername("William")
                .setPassword(TEST_PASSWORD)
                .addRole(role)
                .build();
        userService.save(user1);

        int initSize = userService.findAll().size();

        userService.removeUser(user1);
        Assert.assertTrue(userService.findAll().size() < initSize);
    }
}

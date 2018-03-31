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

import javax.naming.AuthenticationException;
import java.sql.Connection;

import static database.Constants.Roles.ADMINISTRATOR;

public class UserRepositoryMySQLTest {

    public static final String TEST_USERNAME = "mihaidornea";
    public static final String TEST_PASSWORD = "TestPass2word";
    private static UserRepository userRepository;
    private static RightsRolesRepository rightsRolesRepository;
    private static Connection connection;

    @BeforeClass
    public static void setUp(){
        connection = new DBConnectionFactory().getConnectionWrapper().getConnection();
        rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);

    }

    @Before
    public void cleanUp() { userRepository.removeAll(); }

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
        Assert.assertTrue(userRepository.save(user));
    }

    @Test
    public void findByUsernameAndPassword() throws AuthenticationException {
        Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        User user = new UserBuilder().setUsername(TEST_USERNAME)
                .setPassword(TEST_PASSWORD)
                .addRole(role)
                .build();
        userRepository.save(user);
        Notification<User> notification = userRepository.findByUsernameAndPassword(TEST_USERNAME, TEST_PASSWORD);
        System.out.println(notification.getFormattedErrors());
        Assert.assertNotNull(notification.getResult());
    }


   @Test
   public void findByUsername(){
       Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
       User user = new UserBuilder().setUsername(TEST_USERNAME)
               .setPassword(TEST_PASSWORD)
               .addRole(role)
               .build();
        userRepository.save(user);
        Notification<User> notification = userRepository.findByUsername(user.getUsername());
        Assert.assertFalse(notification.hasErrors());
   }

   @Test
   public void update(){
       Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
       User user = new UserBuilder().setUsername(TEST_USERNAME)
               .setPassword(TEST_PASSWORD)
               .addRole(role)
               .build();
       userRepository.save(user);
       user.setUsername("mihaidornea2");
       Assert.assertTrue(userRepository.update(user));
   }

   @Test
    public void removeAll(){
       userRepository.removeAll();
       Assert.assertTrue(userRepository.findAll().isEmpty());
   }

   @Test
    public void removeUser(){
       userRepository.removeAll();
       Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
       User user = new UserBuilder().setUsername(TEST_USERNAME)
               .setPassword(TEST_PASSWORD)
               .addRole(role)
               .build();
       userRepository.save(user);

       User user1 = new UserBuilder().setUsername("William")
               .setPassword(TEST_PASSWORD)
               .addRole(role)
               .build();
       userRepository.save(user1);

       int initSize = userRepository.findAll().size();

       userRepository.removeUser(user1);
       Assert.assertTrue(userRepository.findAll().size() < initSize);
   }
}

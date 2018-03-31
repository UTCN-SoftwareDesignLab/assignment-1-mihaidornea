package repository.security;

import database.DBConnectionFactory;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Rights.CREATE_USER;
import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.EMPLOYEE;

public class RightsRolesRepositoryMySQLTest {

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
    public void cleanUp(){
        rightsRolesRepository.removeAll();
    }

    @Test
    public void addRole(){
        rightsRolesRepository.removeAll();
        rightsRolesRepository.addRole(ADMINISTRATOR);
        Assert.assertNotNull(rightsRolesRepository.findRoleByTitle(ADMINISTRATOR));
    }

    @Test
    public void addRight(){
        rightsRolesRepository.removeAll();
        rightsRolesRepository.addRight(CREATE_USER);
        Assert.assertNotNull(rightsRolesRepository.findRightByTitle(CREATE_USER));
    }

    @Test
    public void addRolesToUser(){
        userRepository.removeAll();
        rightsRolesRepository.removeAll();
        rightsRolesRepository.addRole(EMPLOYEE);
        rightsRolesRepository.addRole(ADMINISTRATOR);
        Role role1 = rightsRolesRepository.findRoleByTitle(EMPLOYEE);
        Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role);
        User user = new UserBuilder().setUsername(TEST_USERNAME)
                .setPassword(TEST_PASSWORD)
                .setRoles(roles)
                .build();
        userRepository.save(user);
        Assert.assertTrue(user.getRoles().size() == 2);
    }

    @Test
    public void findRoleById(){
        rightsRolesRepository.removeAll();
        rightsRolesRepository.addRole(ADMINISTRATOR);
        Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        Assert.assertNotNull(rightsRolesRepository.findRoleById(role.getId()));
    }

    @Test
    public void findRightByTitle(){
        rightsRolesRepository.removeAllRights();
        rightsRolesRepository.addRight(CREATE_USER);
        Assert.assertNotNull(rightsRolesRepository.findRightByTitle(CREATE_USER));
    }

    @Test
    public void findRolesForUser(){
        userRepository.removeAll();
        rightsRolesRepository.removeAll();
        rightsRolesRepository.addRole(EMPLOYEE);
        rightsRolesRepository.addRole(ADMINISTRATOR);
        Role role1 = rightsRolesRepository.findRoleByTitle(EMPLOYEE);
        Role role = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role);
        User user = new UserBuilder().setUsername(TEST_USERNAME)
                .setPassword(TEST_PASSWORD)
                .setRoles(roles)
                .build();
        userRepository.save(user);
        Assert.assertTrue(rightsRolesRepository.findRolesForUser(user.getId()).size() > 0);
    }
}

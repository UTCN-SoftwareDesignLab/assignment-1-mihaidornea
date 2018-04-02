package service.user;

import model.Role;
import model.User;
import model.validation.Notification;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import javax.naming.AuthenticationException;
import java.security.MessageDigest;

public class AuthenticationServiceMySQL implements AuthenticationService{

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AuthenticationServiceMySQL(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<User> login(String username, String password) throws AuthenticationException {
        return userRepository.findByUsernameAndPassword(username, password);
    }


    //TODO: IMPLEMENT THE LOGOUT IN THE CONTROLLER
    @Override
    public boolean logout(User user) {
        return false;
    }

    @Override
    public Role getUserRole(String username, String password) throws AuthenticationException{
        User user = userRepository.findByUsernameAndPassword(username,password).getResult();
        Role role = user.getRoles().get(0);
        return role;
    }

    @Override
    public Long getUserId(String username, String password) throws AuthenticationException {
        User user = userRepository.findByUsernameAndPassword(username,password).getResult();
        return user.getId();
    }

}

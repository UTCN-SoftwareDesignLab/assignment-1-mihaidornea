package service.user;

import model.Role;
import model.User;
import model.validation.Notification;

import javax.naming.AuthenticationException;

public interface AuthenticationService {

    Notification<User> login (String username, String password) throws AuthenticationException;

    Role getUserRole(String username, String password) throws AuthenticationException;

    boolean logout (User user);

}

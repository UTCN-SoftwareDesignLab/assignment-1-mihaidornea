package service.user;

import model.Role;
import model.User;
import model.validation.Notification;

import javax.naming.AuthenticationException;
import java.util.List;

public interface UserService {

    List<User> findAll();

    boolean save(User user);

    boolean update(User user);

    void removeAll();

    User findById(Long id);

    User findByUsername(String username);

    void removeUser(User user);

    Role findRoleByName(String name);

    void addActivity(Long id, String activity);

}

package repository.user;

import model.User;
import model.validation.Notification;

import javax.naming.AuthenticationException;
import java.util.List;

public interface UserRepository {

    List<User> findAll();

    Notification<User> findByUsernameAndPassword(String username, String password) throws AuthenticationException;

    boolean save(User user);

    boolean update(User user);

    User findById(Long id);

    User findByUsername(String username);

    void addActivity(Long id, String activity);

    void removeUser(User user);

    void removeAll();

}

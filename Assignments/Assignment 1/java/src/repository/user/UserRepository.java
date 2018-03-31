package repository.user;

import model.Report;
import model.User;
import model.validation.Notification;

import javax.naming.AuthenticationException;
import java.sql.Date;
import java.util.List;

public interface UserRepository {

    List<User> findAll();

    Notification<User> findByUsernameAndPassword(String username, String password) throws AuthenticationException;

    boolean save(User user);

    boolean update(User user);

    Notification<User> findById(Long id);

    Notification<User> findByUsername(String username);

    void addActivity(Long id, String activity);

    void removeUser(User user);

    void removeAll();

    void removeAllActivities();

    Report getActivities(Long id, Date date);

}

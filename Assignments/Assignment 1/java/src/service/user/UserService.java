package service.user;

import model.Report;
import model.Role;
import model.User;
import model.validation.Notification;

import javax.naming.AuthenticationException;
import java.sql.Date;
import java.util.List;

public interface UserService {

    List<User> findAll();

    Notification<Boolean> save(User user);

    Notification<Boolean> update(User user);

    void removeAll();

    Notification<User> findById(Long id);

    Notification<User> findByUsername(String username);

    void removeUser(User user);

    Role findRoleByName(String name);

    void addActivity(Long id, String activity);

    Report getActivities (Long id, Date date);

}

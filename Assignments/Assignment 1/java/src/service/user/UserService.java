package service.user;

import model.User;
import model.validation.Notification;

import javax.naming.AuthenticationException;
import java.util.List;

public interface UserService {

    List<User> findAll();

    boolean save(User user);

    boolean update(User user);

    void removeAll();

}

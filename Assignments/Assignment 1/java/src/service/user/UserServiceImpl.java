package service.user;

import model.User;
import model.validation.Notification;
import repository.user.UserRepository;

import javax.naming.AuthenticationException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){ this.userRepository = userRepository; }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean update(User user) {
        return userRepository.update(user);
    }

    @Override
    public void removeAll() {
        userRepository.removeAll();
    }
}

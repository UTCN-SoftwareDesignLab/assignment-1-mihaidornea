package service.user;

import model.Report;
import model.Role;
import model.User;
import model.validation.Notification;
import model.validation.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import javax.naming.AuthenticationException;
import java.sql.Date;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RightsRolesRepository rightsRolesRepository;

    public UserServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository){
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository; }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Notification<Boolean> save(User user) {
        UserValidator userValidator = new UserValidator(user);
        boolean goodUser = userValidator.validate();
        Notification<Boolean> notification = new Notification<>();
        if (!goodUser){
            userValidator.getErrors().forEach(notification::addError);
            notification.setResult(Boolean.FALSE);
        } else {
            notification.setResult(userRepository.save(user));
        }
        return notification;
    }

    @Override
    public Notification<Boolean> update(User user) {
        UserValidator userValidator = new UserValidator(user);
        boolean goodUser = userValidator.validate();
        Notification<Boolean> notification = new Notification<>();
        if (!goodUser){
            userValidator.getErrors().forEach(notification::addError);
            notification.setResult(Boolean.FALSE);
        } else {
            notification.setResult(userRepository.update(user));
        }
        return notification;
    }

    @Override
    public void removeAll() {
        userRepository.removeAll();
    }

    @Override
    public Notification<User> findById(Long id){
       return userRepository.findById(id);
    }

    @Override
    public Notification<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void removeUser(User user) {
        userRepository.removeUser(user);
    }

    @Override
    public void addActivity(Long id, String activity) {
        userRepository.addActivity(id, activity);
    }

    @Override
    public Role findRoleByName(String name){
        return rightsRolesRepository.findRoleByTitle(name);
    }

    @Override
    public Report getActivities(Long id, Date date) {
        return userRepository.getActivities(id, date);
    }
}

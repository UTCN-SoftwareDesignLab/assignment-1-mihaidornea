package service.user;

import model.Role;
import model.User;
import model.validation.Notification;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import javax.naming.AuthenticationException;
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

    @Override
    public User findById(Long id){
       return userRepository.findById(id);
    }

    @Override
    public User findByUsername(String username) {
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
}

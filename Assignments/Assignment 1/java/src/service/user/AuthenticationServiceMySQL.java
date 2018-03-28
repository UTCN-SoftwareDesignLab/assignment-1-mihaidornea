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
        return userRepository.findByUsernameAndPassword(username, encodePassword(password));
    }


    //TODO: IMPLEMENT THE LOGOUT IN THE CONTROLLER
    @Override
    public boolean logout(User user) {
        return false;
    }

    @Override
    public Role getUserRole(String username, String password) throws AuthenticationException{
        User user = userRepository.findByUsernameAndPassword(username, encodePassword(password)).getResult();
        Role role = user.getRoles().get(0);
        return role;
    }

    private String encodePassword(String password){
     try{
         MessageDigest digest = MessageDigest.getInstance("SHA-256");
         byte[] hash = digest.digest(password.getBytes("UTF-8"));
         StringBuilder hexString = new StringBuilder();

        for(int i = 0; i < hash.length; i++){
             String hex = Integer.toHexString(0xff & hash[i]);
             if (hex.length() == 1) hexString.append('0');
             hexString.append(hex);
         }

         return hexString.toString();
     } catch (Exception ex){
         throw new RuntimeException(ex);
     }
    }

}

package repository.user;

import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import repository.security.RightsRolesRepository;

import javax.naming.AuthenticationException;
import java.awt.*;
import java.security.MessageDigest;
import java.sql.*;
import java.util.List;

import static database.Constants.Tables.USER;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;

    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) throws AuthenticationException {
        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "SELECT * FROM `" + USER + "`WHERE `username`=\'" + username + "\' AND `password`=\'" + password + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            if (userResultSet.next()) {
                User user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
                findByUsernameAndPasswordNotification.setResult(user);
                return findByUsernameAndPasswordNotification;
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid email or password!");
                return findByUsernameAndPasswordNotification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuthenticationException();
        }
    }

    @Override
    public boolean save(User user) {
        try{
            PreparedStatement insertUserStamement = connection
                    .prepareStatement("INSERT INTO user values(null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStamement.setString(1, user.getUsername());
            insertUserStamement.setString(2, encodePassword(user.getPassword()));

            int affectedRows = insertUserStamement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected");
            }

            ResultSet rs = insertUserStamement.getGeneratedKeys();
            if (rs.next()){
                user.setId(rs.getLong(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained");
            }

            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }

    public boolean update(User user){
        try{
            PreparedStatement updateStatement = connection
                    .prepareStatement("UPDATE user SET username = ?, password = ? WHERE id = ?");
            updateStatement.setString(1, user.getUsername());
            updateStatement.setString(2, user.getPassword());
            updateStatement.setLong(3, user.getId());
            updateStatement.executeUpdate();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void removeAll() {
        try{
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM USER WHERE id >= 0";
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
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

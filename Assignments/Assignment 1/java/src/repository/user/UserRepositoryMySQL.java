package repository.user;

import javafx.stage.Stage;
import model.Activity;
import model.Report;
import model.User;
import model.builder.ActivityBuilder;
import model.builder.UserBuilder;
import model.validation.Notification;
import repository.EntityNotFoundException;
import repository.security.RightsRolesRepository;

import javax.naming.AuthenticationException;
import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
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
        List<User> users = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM user";
            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()){
                users.add(getUserFromResultSet(rs));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) throws AuthenticationException {
        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "SELECT * FROM `" + USER + "` WHERE `username`=\'" + username + "\' AND `password`=\'" + encodePassword(password) + "\'";
            System.out.println(fetchUserSql);
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            if (userResultSet.next()) {
                User user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setId(userResultSet.getLong("id"))
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
            updateStatement.setString(2, encodePassword(user.getPassword()));
            updateStatement.setLong(3, user.getId());
            updateStatement.executeUpdate();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Notification<User> findById(Long id){
        Notification<User> findByIdNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM user WHERE id=" + id;
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                User user = getUserFromResultSet(rs);
                findByIdNotification.setResult(user);
                return findByIdNotification;
            } else {
                findByIdNotification.addError("Invalid id!");
                return findByIdNotification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Notification<User> findByUsername(String username){
        Notification<User> findNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM `" + USER + "` WHERE `username`=\'" + username +"\'";
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()){
                User user = getUserFromResultSet(rs);
                findNotification.setResult(user);
                return findNotification;
            } else {
                findNotification.addError("Invalid username");
                return findNotification;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
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

    @Override
    public void removeUser(User user){
        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement("DELETE FROM USER WHERE id = ?");
            updateStatement.setLong(1, user.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void removeAllActivities(){
        try{
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM user_report WHERE id >= 0";
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addActivity(Long id, String activity) {
        try {
            PreparedStatement insertUserStamement = connection
                    .prepareStatement("INSERT INTO user_report values(null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStamement.setLong(1, id);
            insertUserStamement.setString(2, activity);
            insertUserStamement.setDate(3, new Date(new java.util.Date().getTime()));
            insertUserStamement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Report getActivities(Long id, Date date) {
        Report report = new Report();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM bank.user_report WHERE user_id =" + id + " AND date >" + date;
            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()){
                Activity activity = getActivityFromResultSet(rs);
                report.addActivities(activity);
                System.out.println(activity.getAction());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return report;
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

    private User getUserFromResultSet(ResultSet rs) throws SQLException{
        return new UserBuilder()
                .setUsername(rs.getString("username"))
                .setId(rs.getLong("id"))
                .build();
    }

    private Activity getActivityFromResultSet(ResultSet rs) throws SQLException{
        Activity activity = new ActivityBuilder()
                                    .setAction(rs.getString("action"))
                                    .setUserId(rs.getLong("user_id"))
                                    .setDate(rs.getDate("date"))
                                    .build();
        System.out.println(activity.getAction());
        return activity;
    }

}

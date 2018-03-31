package controller;

import componentFactory.ComponentFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Activity;
import model.Report;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import org.w3c.dom.Text;
import service.user.UserService;
import service.user.UserServiceImpl;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.EMPLOYEE;

public class AdministratorViewController {


    private MainController mainController;
    private ComponentFactory componentFactory;
    private UserService userService;

    @FXML
    private TextArea administratorTableView;

    @FXML
    private Button createUserButton;

    @FXML
    private Button updateUserButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Button findUserButton;

    @FXML
    private Button generateUserReportButton;

    public AdministratorViewController(MainController mainController) throws IOException {
        this.mainController = mainController;
        this.componentFactory = mainController.getComponentFactory();
        this.userService = new UserServiceImpl(componentFactory.getUserRepository(), componentFactory.getRightsRolesRepository());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdministratorView.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Bank Application");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        getAllUsers();

    }

    private enum Roles {ADMINISTRATOR, EMPLOYEE}


    public void getAllUsers() {
        List<User> users = users = userService.findAll();
        String s = new String("");
        for (User u : users){
            s = s.concat(u.getId() + " " + u.getUsername() + "\n");
        }
        administratorTableView.setText(s);
    }



    @FXML
    public void createUser() {
        Dialog<UserFields> dialog = new Dialog<>();
        dialog.setTitle("Add new User");
        dialog.setHeaderText("Please fill the following fields");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField username = new TextField("Username");
        TextField password = new TextField("Password");
        ComboBox<Roles> roles = new ComboBox<>();
        roles.getItems().addAll(Roles.values());
        dialogPane.setContent(new VBox(8, username, password, roles));
        dialog.show();
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    Role role;
                    if (roles.getValue() != Roles.ADMINISTRATOR) {
                        role = userService.findRoleByName(ADMINISTRATOR);
                    } else role = userService.findRoleByName(EMPLOYEE);

                    UserFields userFields = new UserFieldsBuilder()
                            .setUsername(username.getText())
                            .setPassword(password.getText())
                            .setRole(role)
                            .build();

                    User user = new UserBuilder()
                            .addRole(role)
                            .setUsername(userFields.getUsername())
                            .setPassword(userFields.getPassword())
                            .build();

                    Notification<Boolean> notification = userService.save(user);
                    if (!notification.getFormattedErrors().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Creating User Failure!");
                        alert.setContentText(notification.getFormattedErrors());
                        alert.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            getAllUsers();
            return null;
        });

    }

    @FXML
    public void updateUser() {
        Dialog<UserFields> dialog = new Dialog<>();
        dialog.setTitle("Add new User");
        dialog.setHeaderText("Please fill the following fields");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField id = new TextField("ID");
        TextField newUsername = new TextField("New Username");
        TextField password = new TextField("New Password");
        dialogPane.setContent(new VBox(8, id, newUsername, password));
        dialog.show();
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {

                    UserFields userFields = new UserFieldsBuilder()
                            .setId(Long.parseLong(id.getText()))
                            .setUsername(newUsername.getText())
                            .setPassword(password.getText())
                            .build();

                    Notification<User> findNotification = new Notification<>();
                    findNotification = userService.findById(userFields.getId());
                    if (!findNotification.getFormattedErrors().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Find User Failure");
                        alert.setContentText(findNotification.getFormattedErrors());
                        alert.show();
                    } else {
                        User user = findNotification.getResult();
                        user.setPassword(userFields.getPassword());
                        user.setUsername(userFields.getUsername());

                        Notification<Boolean> notification = userService.update(user);
                        if (!notification.getFormattedErrors().isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Updating User Failure!");
                            alert.setContentText(notification.getFormattedErrors());
                            alert.show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            getAllUsers();
            return null;
        });

    }

    @FXML
    public void deleteUser() {
        Dialog<UserFields> dialog = new Dialog<>();
        dialog.setTitle("Delete User");
        dialog.setHeaderText("Please fill the following fields");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField username = new TextField("username");
        dialogPane.setContent(new VBox(8, username));
        dialog.show();
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {

                    UserFields userFields = new UserFieldsBuilder()
                            .setUsername(username.getText())
                            .build();

                    Notification<User> findNotification = new Notification<>();
                    findNotification = userService.findByUsername(userFields.getUsername());
                    if (!findNotification.getFormattedErrors().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Find User Failure");
                        alert.setContentText(findNotification.getFormattedErrors());
                        alert.show();
                    } else
                        userService.removeUser(findNotification.getResult());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            getAllUsers();
            return null;
        });
    }

    @FXML
    public void findUser() {

    }

    @FXML
    public void generateUserReport() {
        Dialog<UserFields> dialog = new Dialog<>();
        dialog.setTitle("Delete User");
        dialog.setHeaderText("Please fill the following fields");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField employeeId = new TextField("Employee ID");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        dialogPane.setContent(new VBox(8, employeeId, datePicker));
        dialog.show();
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    Date date = Date.valueOf(datePicker.getValue());
                    System.out.println(date);
                    Report report = userService.getActivities(Long.parseLong(employeeId.getText()), date);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Report");
                    alert.setHeaderText("Activities:");
                    System.out.println(report.toString());
                    alert.setContentText(report.toString());
                    alert.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }
}

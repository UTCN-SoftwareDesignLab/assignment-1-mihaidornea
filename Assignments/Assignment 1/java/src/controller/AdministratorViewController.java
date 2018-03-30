package controller;

import componentFactory.ComponentFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import org.w3c.dom.Text;
import service.user.UserService;
import service.user.UserServiceImpl;

import java.io.IOException;

import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.EMPLOYEE;

public class AdministratorViewController {


    private MainController mainController;
    private ComponentFactory componentFactory;
    private UserService userService;

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
    }

    private enum Roles {ADMINISTRATOR, EMPLOYEE};

    @FXML
    public void createUser(){
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
            if (button == ButtonType.OK){
                try {
                    Role role;
                    if (roles.getValue() != Roles.ADMINISTRATOR){
                         role = userService.findRoleByName(ADMINISTRATOR);
                    } else  role = userService.findRoleByName(EMPLOYEE);

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

                    userService.save(user);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        });

    }

    @FXML
    public void updateUser(){
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
            if (button == ButtonType.OK){
                try {

                    UserFields userFields = new UserFieldsBuilder()
                            .setId(Long.parseLong(id.getText()))
                            .setUsername(newUsername.getText())
                            .setPassword(password.getText())
                            .build();

                    User user = userService.findById(userFields.getId());
                    user.setPassword(userFields.getPassword());
                    user.setUsername(userFields.getUsername());

                    userService.update(user);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        });

    }

    @FXML
    public void deleteUser(){
        Dialog<UserFields> dialog = new Dialog<>();
        dialog.setTitle("Delete User");
        dialog.setHeaderText("Please fill the following fields");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField username = new TextField("username");
        dialogPane.setContent(new VBox(8,username));
        dialog.show();
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK){
                try {

                    UserFields userFields = new UserFieldsBuilder()
                            .setUsername(username.getText())
                            .build();

                    User user = userService.findByUsername(userFields.getUsername());
                    userService.removeUser(user);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        });
    }

    @FXML
    public void findUser(){

    }

    @FXML
    public void generateUserReport(){

    }



}

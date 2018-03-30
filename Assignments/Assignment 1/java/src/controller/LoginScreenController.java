package controller;

import java.io.IOException;
import componentFactory.ComponentFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Role;
import model.User;
import model.validation.Notification;
import service.user.AuthenticationService;

import javax.naming.AuthenticationException;

import static database.Constants.Roles.ADMINISTRATOR;

public class LoginScreenController {

    private MainController main;
    private ComponentFactory componentFactory;
    private AuthenticationService authenticationService;
    private Role userRole;
    private Long userId;
    @FXML
    Button loginButton;

    @FXML
    TextField usernameTextField;

    @FXML
    TextField passwordTextField;

    public LoginScreenController(MainController mainController) throws IOException{
        this.main = mainController;
        this.componentFactory = mainController.getComponentFactory();
        this.authenticationService = componentFactory.getAuthenticationService();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginScreen.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Bank Application");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    @FXML
    void login() throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        Notification<User> loginNotification = null;
        try {
            loginNotification = authenticationService.login(username, password);
            userRole = authenticationService.getUserRole(username, password);
            userId = authenticationService.getUserId(username, password);

        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        if (loginNotification != null) {
            if (loginNotification.hasErrors()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Login Failure!");
                alert.setContentText(loginNotification.getFormattedErrors());
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lgoin");
                alert.setHeaderText("Login was successful");
                alert.show();
            }
        }

        main.setUserRole(userRole);
        main.openMainWindow();
        main.setUserId(userId);

        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }
}



package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.nio.sctp.Association;
import com.sun.tools.javac.Main;
import componentFactory.ComponentFactory;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.Role;
import model.User;
import model.validation.Notification;
import service.user.AuthenticationService;

import javax.naming.AuthenticationException;
import javax.swing.*;

public class LoginScreenController {

    private MainController main;
    private ComponentFactory componentFactory;
    private AuthenticationService authenticationService;
    private static Role userRole;
    @FXML
    Button loginButton;

    @FXML
    TextField usernameTextField;

    @FXML
    TextField passwordTextField;



    public LoginScreenController(){
        init();
        this.componentFactory = ComponentFactory.instance();
        this.authenticationService = componentFactory.getAuthenticationService();
    }

    @FXML
    void login() throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        Notification<User> loginNotification = null;
        try {
            loginNotification = authenticationService.login(username, password);
            userRole = authenticationService.getUserRole(username, password);
            main.setUserRole(userRole);
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
        main.openMainWindow();
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();

    }

    private void init(){
        this.main = MainController.initialise();
    }
}



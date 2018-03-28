package controller;

import com.sun.tools.javac.Main;
import componentFactory.ComponentFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Role;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;

import java.io.IOException;

import static database.Constants.Roles.ADMINISTRATOR;

public class MainController {


    private final ComponentFactory componentFactory;
    private Role userRole;
    private static MainController instance;

    private MainController() {
        this.componentFactory = ComponentFactory.instance();
    }

    public static MainController initialise(){
        if (instance != null) {
            return instance;
        } else {
            instance = new MainController();
        }
        return  instance;
    }


    public void setUserRole(Role role){
        this.userRole = role;
        System.out.println(role.toString());
    }

    public void openMainWindow() {
        try {
           if (userRole.getRole() == ADMINISTRATOR) {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdministratorView.fxml"));
               AdministratorViewController administratorViewController = new AdministratorViewController();
               loader.setController(administratorViewController);
               Parent root = loader.load();
               Stage primaryStage = new Stage();
               primaryStage.setTitle("Bank Application");
               primaryStage.setScene(new Scene(root, 300, 275));
               primaryStage.show();
           } else {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmployeeView.fxml"));
               EmployeeViewController employeeViewController = new EmployeeViewController();
               loader.setController(employeeViewController);
               Parent root = loader.load();
               Stage primaryStage = new Stage();
               primaryStage.setTitle("Bank Application");
               primaryStage.setScene(new Scene(root, 300, 275));
               primaryStage.show();
           }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


}

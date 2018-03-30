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
import static database.Constants.Roles.EMPLOYEE;

public class MainController {


    private Role userRole;
    private Long userId;
    private final ComponentFactory componentFactory;

    private AdministratorViewController administratorViewController;
    private EmployeeViewController employeeViewController;

    public MainController(ComponentFactory componentFactory) throws IOException{
        this.componentFactory = componentFactory;
        LoginScreenController loginScreenController = new LoginScreenController(this);
    }

    public void openMainWindow() throws IOException{
        if (userRole.getRole().equals(ADMINISTRATOR))
            administratorViewController = new AdministratorViewController(this);
        else if (userRole.getRole().equals(EMPLOYEE))
            employeeViewController = new EmployeeViewController(this, userId);
    }

   public ComponentFactory getComponentFactory(){
        return this.componentFactory;
   }

    public void setUserRole(Role role){
        this.userRole = role;
    }

    public void setUserId(Long id){
        this.userId = id;
    }
}

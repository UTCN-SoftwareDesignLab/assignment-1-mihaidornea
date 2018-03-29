package controller;

import componentFactory.ComponentFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.user.UserService;
import service.user.UserServiceImpl;

import java.io.IOException;

import static database.Constants.Roles.ADMINISTRATOR;

public class AdministratorViewController {


    private MainController mainController;
    private ComponentFactory componentFactory;
    private UserService userService;


    public AdministratorViewController(MainController mainController) throws IOException {
        this.mainController = mainController;
        this.componentFactory = mainController.getComponentFactory();
        this.userService = new UserServiceImpl(componentFactory.getUserRepository());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdministratorView.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Bank Application");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

}

package launcher;

import componentFactory.ComponentFactory;
import controller.LoginScreenController;
import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Client;
import model.User;
import model.builder.ClientBuilder;
import model.builder.UserBuilder;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import service.user.AuthenticationService;

import static database.Constants.Roles.ADMINISTRATOR;


public class Launcher extends Application {

    private static ComponentFactory componentFactory;


    public static void main (String[] args) {
        componentFactory = ComponentFactory.instance();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainController mainController = new MainController(componentFactory);
    }
}

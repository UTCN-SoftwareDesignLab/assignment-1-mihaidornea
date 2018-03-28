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

    private static UserRepository userRepository;
    private static RightsRolesRepository rightsRolesRepository;
    private static AuthenticationService authenticationService;
    private static ComponentFactory componentFactory;



    public static void main (String[] args) {
        componentFactory = ComponentFactory.instance();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginScreen.fxml"));
        LoginScreenController loginScreenController = new LoginScreenController();
        loader.setController(loginScreenController);
        Parent root = loader.load();
        primaryStage.setTitle("Bank Application");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }
}

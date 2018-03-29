package controller;


import componentFactory.ComponentFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Client;
import model.builder.ClientBuilder;
import service.client.ClientService;
import service.client.ClientServiceImpl;

import java.io.IOException;

public class EmployeeViewController {

    private MainController main;
    private ComponentFactory componentFactory;
    private ClientService clientService;


    @FXML
    private Button addCustomerButton;

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private Button findCustomerButton;

    @FXML
    private Button processUtilitiesButton;

    @FXML
    private Button createAccountButton;

    @FXML
    private Button deleteAccountButton;

    @FXML
    private Button transferMoneyButton;


    public EmployeeViewController(MainController mainController) throws IOException {
        this.main = mainController;
        this.componentFactory = mainController.getComponentFactory();
        this.clientService = new ClientServiceImpl(componentFactory.getClientRepository());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmployeeView.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Bank Application");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    @FXML
    public void addCustomer(){
        Dialog<ClientFields> dialog = new Dialog<>();
        dialog.setTitle("Add New Client");
        dialog.setHeaderText("Please fill the following fields");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField clientName = new TextField("Name");
        TextField identificationNumber = new TextField("Identification Number");
        TextField personalCode = new TextField("Personal Code");
        TextField address = new TextField("Address");
        dialogPane.setContent(new VBox(8, clientName, identificationNumber, personalCode,address));
        dialog.show();
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    ClientFields results = new ClientFieldsBuilder()
                            .setName(clientName.getText())
                            .setAddress(address.getText())
                            .setIdentificationNumber(Long.parseLong(identificationNumber.getText()))
                            .setPersonalCodeNumber(Long.parseLong(personalCode.getText()))
                            .build();

                        Client client = new ClientBuilder()
                                .setName(results.getName())
                                .setIdentityNumeber(results.getIdentificationNumber())
                                .setPersonalNumericalCode(results.getPersonalCode())
                                .setAddress(results.getAddress())
                                .build();

                        System.out.println(client.getAddress() + client.getIdentityNumber());

                        clientService.save(client);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
        });
    }


    @FXML
    public void updateCustomer(){
        Dialog<ClientFields> dialog = new Dialog<>();
        dialog.setTitle("Update Client");
        dialog.setHeaderText("Please fill the following fields");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField personalCode = new TextField("Personal Code");
        TextField clientName = new TextField("Name");
        TextField address = new TextField("Address");
        dialogPane.setContent(new VBox(8, personalCode, clientName, address));
        dialog.show();
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    ClientFields results = new ClientFieldsBuilder()
                            .setName(clientName.getText())
                            .setAddress(address.getText())
                            .setPersonalCodeNumber(Long.parseLong(personalCode.getText()))
                            .build();

                    Client client = clientService.findByPersonalCode(results.getPersonalCode());
                    client.setName(results.getName());
                    client.setAddress(results.getAddress());
                    clientService.update(client);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }

    @FXML
    public void deleteCustomer(){
        Dialog<ClientFields> dialog = new Dialog<>();
        dialog.setTitle("Delete Client");
        dialog.setHeaderText("Please fill the following fields");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField personalCode = new TextField("Personal Code");
        dialogPane.setContent(new VBox(8, personalCode));
        dialog.show();
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    ClientFields results = new ClientFieldsBuilder()
                            .setPersonalCodeNumber(Long.parseLong(personalCode.getText()))
                            .build();

                    Client client = clientService.findByPersonalCode(results.getPersonalCode());
                    clientService.remove(client);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }

    @FXML
    public void findCustomer(){


    }

    @FXML
    public void processUtilities(){

    }

    @FXML
    public void createAccount(){

    }

    @FXML
    public void deleteAccount(){

    }

    @FXML
    public void transferMoney(){

    }



}

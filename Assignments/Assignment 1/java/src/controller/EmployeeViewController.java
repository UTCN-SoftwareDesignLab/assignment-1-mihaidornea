package controller;


import componentFactory.ComponentFactory;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Account;
import model.Client;
import model.User;
import model.builder.AccountBuilder;
import model.builder.ClientBuilder;
import org.w3c.dom.Text;
import service.account.AccountService;
import service.account.AccountServiceImpl;
import service.client.ClientService;
import service.client.ClientServiceImpl;
import service.user.UserService;
import service.user.UserServiceImpl;

import java.io.IOException;
import java.util.Date;

public class EmployeeViewController {

    private Long id;
    private MainController main;
    private ComponentFactory componentFactory;
    private ClientService clientService;
    private AccountService accountService;
    private UserService userService;

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

    private enum Types {SAVINGS, CREDIT}

    public EmployeeViewController(MainController mainController, Long id) throws IOException {
        this.id = id;
        this.main = mainController;
        this.componentFactory = mainController.getComponentFactory();
        this.clientService = new ClientServiceImpl(componentFactory.getClientRepository());
        this.accountService = new AccountServiceImpl(componentFactory.getAccountRepository());
        this.userService = new UserServiceImpl(componentFactory.getUserRepository(), componentFactory.getRightsRolesRepository());
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

                        clientService.save(client);
                        userService.addActivity(id, "Added Customer");

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
                    userService.addActivity(id, "Updated Customer");

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
                    userService.addActivity(id, "Deleted Customer");

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
        Dialog<ClientFields> dialog = new Dialog<>();
        dialog.setTitle("Process Utilities");
        dialog.setHeaderText("Please fill the following fields");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField accountIdentificationNumber = new TextField("Identification Number");
        TextField amountOfMoney = new TextField("Amount of Money to Pay");
        dialogPane.setContent(new VBox(8, accountIdentificationNumber, amountOfMoney));
        dialog.show();
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    AccountFields accountFields = new AccountFieldsBuilder()
                            .setIdentificationNumber(Long.parseLong(accountIdentificationNumber.getText()))
                            .setMoney(Long.parseLong(amountOfMoney.getText()))
                            .build();

                    Account account = accountService.findByIdentificationNumber(accountFields.getIdentificationNumber());
                    accountService.payUtilityBill(accountFields.getMoney(), account);
                    userService.addActivity(id, "Processed Bill");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }

    @FXML
    public void createAccount(){

        Dialog<ClientFields> dialog = new Dialog<>();
        dialog.setTitle("Create New Account");
        dialog.setHeaderText("Please fill the following fields");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField accountIdentificationNumber = new TextField("Identification Number");
        ComboBox<Types> typeOfAccount = new ComboBox<>();
        typeOfAccount.getItems().addAll(Types.CREDIT, Types.SAVINGS);
        TextField amountOfMoney = new TextField("Amount of Money");
        TextField clientId = new TextField("Client Personal Code Number");
        dialogPane.setContent(new VBox(8, clientId, accountIdentificationNumber, typeOfAccount ,amountOfMoney));
        dialog.show();
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    String type;
                    if (typeOfAccount.getValue() == Types.SAVINGS) {
                        type = "Savings";
                    } else type = "Credit";
                    AccountFields accountFields = new AccountFieldsBuilder()
                            .setClientId(Long.parseLong(clientId.getText()))
                            .setIdentificationNumber(Long.parseLong(accountIdentificationNumber.getText()))
                            .setMoney(Long.parseLong(amountOfMoney.getText()))
                            .setType(type)
                            .build();

                    Client client = clientService.findByPersonalCode(accountFields.getClientId());

                    Account account = new AccountBuilder()
                            .setDateOfCreation(new Date())
                            .setClientId(client.getId())
                            .setType(accountFields.getType())
                            .setIdentificationNumber(accountFields.getIdentificationNumber())
                            .setAmountOfMoney(accountFields.getMoney())
                            .build();

                    accountService.save(account);
                    userService.addActivity(id, "Created Account");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        });

    }


    @FXML
    public void deleteAccount(){
        Dialog<ClientFields> dialog = new Dialog<>();
        dialog.setTitle("Delete Account");
        dialog.setHeaderText("Please fill the following fields");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField accountIdentificationNumber = new TextField("Identification Number");
        dialogPane.setContent(new VBox(8, accountIdentificationNumber));
        dialog.show();
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    AccountFields accountFields = new AccountFieldsBuilder()
                            .setIdentificationNumber(Long.parseLong(accountIdentificationNumber.getText()))
                            .build();

                    Account account = accountService.findByIdentificationNumber(accountFields.getIdentificationNumber());
                    accountService.removeAccount(account);
                    userService.addActivity(id, "Deleted Account");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }

    @FXML
    public void transferMoney(){
        Dialog<ClientFields> dialog = new Dialog<>();
        dialog.setTitle("Transfer Money Between Accounts");
        dialog.setHeaderText("Please fill the following fields");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField account1IdentificationNumber = new TextField("Account1 Identification Number");
        TextField account2IdentificationNumber = new TextField("Account2 Identification Number");
        TextField money = new TextField("Money to be transfer");
        dialogPane.setContent(new VBox(8, account1IdentificationNumber, account2IdentificationNumber, money));
        dialog.show();
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    AccountFields accountFields = new AccountFieldsBuilder()
                            .setIdentificationNumber(Long.parseLong(account1IdentificationNumber.getText()))
                            .setSecondIdentificationNumber(Long.parseLong(account2IdentificationNumber.getText()))
                            .setMoney(Long.parseLong(money.getText()))
                            .build();

                    Account account = accountService.findByIdentificationNumber(accountFields.getIdentificationNumber());
                    accountService.removeAccount(account);
                    userService.addActivity(id, "Transferred money");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }



}

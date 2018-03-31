package controller;


import componentFactory.ComponentFactory;
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
import model.validation.Notification;
import repository.EntityNotFoundException;
import service.account.AccountService;
import service.account.AccountServiceImpl;
import service.client.ClientService;
import service.client.ClientServiceImpl;
import service.user.UserService;
import service.user.UserServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeViewController {

    private Long id;
    private MainController main;
    private ComponentFactory componentFactory;
    private ClientService clientService;
    private AccountService accountService;
    private UserService userService;

    @FXML
    private TextArea tableView;

    @FXML
    private Button updateAccountButton;

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
        getAllClients();
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

                        Notification<Boolean> notification = clientService.save(client);
                        if(!notification.getFormattedErrors().isEmpty()){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Creating Client Failure!");
                            alert.setContentText(notification.getFormattedErrors());
                            alert.show();
                        }else userService.addActivity(id, "Added Customer");

                } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            getAllClients();
            return null;
        });
    }

    private void getAllClients(){
        List<Client> clients = clientService.findAll();
        String s = new String();
        for (Client c : clients){
            s = s.concat(c.getId() + " " +c.getName() + " " + c.getPersonalNumericalCode() + " " + c.getIdentityNumber() + " " + c.getAddress() + "\n");
        }
        tableView.setText(s);
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

                    Notification<Client> notification = clientService.findByPersonalCode(results.getPersonalCode());

                    if(!notification.getFormattedErrors().isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Update Client Failure!");
                        alert.setContentText(notification.getFormattedErrors());
                        alert.show();
                    } else {
                        Client client = notification.getResult();
                        client.setName(results.getName());
                        client.setAddress(results.getAddress());
                        Notification<Boolean> notification1 = clientService.update(client);
                        if (!notification1.getFormattedErrors().isEmpty()){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Update Client Failure!");
                            alert.setContentText(notification.getFormattedErrors());
                            alert.show();
                        } else
                            userService.addActivity(id, "Updated Customer");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            getAllClients();
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

                    Notification<Client> notification = clientService.findByPersonalCode(results.getPersonalCode());

                    if(!notification.getFormattedErrors().isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Remove Client Failure!");
                        alert.setContentText(notification.getFormattedErrors());
                        alert.show();
                    } else {
                        Client client = notification.getResult();
                        clientService.remove(client);
                        userService.addActivity(id, "Deleted Customer");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            getAllClients();
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
                    Notification<Account> findNotification = accountService.findByIdentificationNumber(accountFields.getIdentificationNumber());
                    if (!findNotification.getFormattedErrors().isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Processing Utilities Bill Failure!");
                        alert.setContentText(findNotification.getFormattedErrors());
                        alert.show();
                    }
                    else {
                        accountService.payUtilityBill(accountFields.getMoney(), findNotification.getResult());
                        userService.addActivity(id, "Processed Bill");
                    }

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

                    Notification<Client> clientNotification = clientService.findByPersonalCode(accountFields.getClientId());
                    if(!clientNotification.getFormattedErrors().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Remove Client Failure!");
                        alert.setContentText(clientNotification.getFormattedErrors());
                        alert.show();
                    } else {
                        Client client = clientNotification.getResult();

                        Account account = new AccountBuilder()
                                .setDateOfCreation(new Date())
                                .setClientId(client.getId())
                                .setType(accountFields.getType())
                                .setIdentificationNumber(accountFields.getIdentificationNumber())
                                .setAmountOfMoney(accountFields.getMoney())
                                .build();

                        Notification<Boolean> notification = accountService.save(account);
                        if (!notification.getFormattedErrors().isEmpty()){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Creating Account Failure!");
                            alert.setContentText(notification.getFormattedErrors());
                            alert.show();
                        } else {
                            userService.addActivity(id, "Created Account");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        });

    }

    @FXML
    public void updateAccount(){
        Dialog<ClientFields> dialog = new Dialog<>();
        dialog.setTitle("Update Account");
        dialog.setHeaderText("Please fill the following fields");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField accountIdentificationNumber = new TextField("Identification Number");
        TextField money = new TextField("Money");
        dialogPane.setContent(new VBox(8, accountIdentificationNumber, money));
        dialog.show();
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                try {
                    AccountFields accountFields = new AccountFieldsBuilder()
                            .setIdentificationNumber(Long.parseLong(accountIdentificationNumber.getText()))
                            .setMoney(Long.parseLong(money.getText()))
                            .build();
                    Notification<Account> findNotification = accountService.findByIdentificationNumber(accountFields.getIdentificationNumber());
                    if (!findNotification.getFormattedErrors().isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Identification Number");
                        alert.setContentText(findNotification.getFormattedErrors());
                        alert.show();
                    } else {
                        Account account= findNotification.getResult();
                        Notification<Boolean> updateNotification = accountService.update(account);
                        if(updateNotification.getFormattedErrors().isEmpty())
                            userService.addActivity(id, "Updated Account");
                        else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Invalid Money Ammount");
                            alert.setContentText(findNotification.getFormattedErrors());
                            alert.show();
                        }
                    }
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

                    Notification<Account> findNotification = accountService.findByIdentificationNumber(accountFields.getIdentificationNumber());
                    if (!findNotification.getFormattedErrors().isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Identification Number");
                        alert.setContentText(findNotification.getFormattedErrors());
                        alert.show();
                    } else {
                        Account account= findNotification.getResult();
                        accountService.removeAccount(account);
                        userService.addActivity(id, "Deleted Account");
                    }
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

                    Notification<Boolean> transferNotification = accountService.transferMoney(
                            accountFields.getIdentificationNumber()
                            ,accountFields.getSecondIdentificationNumber()
                            ,accountFields.getMoney());
                    if (!transferNotification.getFormattedErrors().isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Identification Number");
                        alert.setContentText(transferNotification.getFormattedErrors());
                        alert.show();
                    } else {
                        accountService.transferMoney(
                                accountFields.getIdentificationNumber()
                                ,accountFields.getSecondIdentificationNumber()
                                ,accountFields.getMoney());
                        userService.addActivity(id, "Transferred money");
                    }
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }



}

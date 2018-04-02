package service.account;

import database.DBConnectionFactory;
import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.builder.ClientBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.EntityNotFoundException;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import service.account.AccountService;
import service.account.AccountServiceImpl;
import service.client.ClientService;
import service.client.ClientServiceImpl;

import java.sql.Connection;
import java.util.Date;

public class AccountServiceImplTest {

    public static final Long IDENTIFICATION_NUMBER_FOR_TEST = 123412L;
    public static final String TYPE_FOR_IDENTIFICATION = "Savings";
    public static final Long MONEY_FOR_TEST = 300L;
    public static final Date DATE_FOR_TEST = new Date();
    public static final String NAME_FOR_TEST = "Mihai";
    public static final String ADDRESS_FOR_TEST = "StrMea";
    public static final Long PERSONAL_CODE_FOR_TEST = 123124L;
    public static final Long CLIENT_IDENTIFICATION_NUMBER_FOR_TEST = 312132L;

    private static ClientRepository clientRepository;
    private static AccountRepository accountRepository;
    private static Connection connection;
    private static AccountService accountService;
    private static ClientService clientService;

    @BeforeClass
    public static void setUp() {
        connection = new DBConnectionFactory().getConnectionWrapper().getConnection();
        clientRepository = new ClientRepositoryMySQL(connection);
        accountRepository = new AccountRepositoryMySQL(connection);
        accountService = new AccountServiceImpl(accountRepository);
        clientService = new ClientServiceImpl(clientRepository);
    }

    @Before
    public void cleanUp() {
        accountRepository.removeAll();
    }

    @Test
    public void save() throws EntityNotFoundException {
        accountRepository.removeAll();
        clientRepository.removeAll();
        Client client = new ClientBuilder()
                .setAddress(ADDRESS_FOR_TEST)
                .setPersonalNumericalCode(PERSONAL_CODE_FOR_TEST)
                .setIdentityNumeber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setName(NAME_FOR_TEST)
                .build();
        clientService.save(client);
        Client client1 = clientService.findByPersonalCode(PERSONAL_CODE_FOR_TEST).getResult();
        Account account = new AccountBuilder()
                .setAmountOfMoney(MONEY_FOR_TEST)
                .setIdentificationNumber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setType(TYPE_FOR_IDENTIFICATION)
                .setClientId(client.getId())
                .setDateOfCreation(new java.sql.Date(DATE_FOR_TEST.getTime()))
                .build();
        Assert.assertTrue(!accountService.save(account).hasErrors());
    }

    @Test
    public void update() throws EntityNotFoundException{
        accountRepository.removeAll();
        clientRepository.removeAll();
        Client client = new ClientBuilder()
                .setAddress(ADDRESS_FOR_TEST)
                .setPersonalNumericalCode(PERSONAL_CODE_FOR_TEST)
                .setIdentityNumeber(CLIENT_IDENTIFICATION_NUMBER_FOR_TEST)
                .setName(NAME_FOR_TEST)
                .build();
        clientService.save(client);
        Client client1 = clientService.findByPersonalCode(PERSONAL_CODE_FOR_TEST).getResult();
        Account account = new AccountBuilder()
                .setAmountOfMoney(MONEY_FOR_TEST)
                .setIdentificationNumber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setType(TYPE_FOR_IDENTIFICATION)
                .setClientId(client.getId())
                .setDateOfCreation(new java.sql.Date(DATE_FOR_TEST.getTime()))
                .build();
        accountService.save(account);
        account.setAmountOfMoney(100L);
        accountService.update(account);
        Assert.assertTrue(accountService.findByIdentificationNumber(IDENTIFICATION_NUMBER_FOR_TEST).getResult().getAmountOfMoney() == 100);
    }

    @Test
    public void findById() throws EntityNotFoundException{
        accountRepository.removeAll();
        clientRepository.removeAll();
        Client client = new ClientBuilder()
                .setAddress(ADDRESS_FOR_TEST)
                .setPersonalNumericalCode(PERSONAL_CODE_FOR_TEST)
                .setIdentityNumeber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setName(NAME_FOR_TEST)
                .build();
        clientService.save(client);
        Client client1 = clientService.findByPersonalCode(PERSONAL_CODE_FOR_TEST).getResult();
        Account account = new AccountBuilder()
                .setAmountOfMoney(MONEY_FOR_TEST)
                .setIdentificationNumber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setType(TYPE_FOR_IDENTIFICATION)
                .setClientId(client.getId())
                .setDateOfCreation(new java.sql.Date(DATE_FOR_TEST.getTime()))
                .build();
        accountService.save(account);
        Assert.assertFalse(accountService.findById(account.getId()).hasErrors());
    }

    @Test
    public void findByIdentificationNumber() throws EntityNotFoundException{
        accountRepository.removeAll();
        clientRepository.removeAll();
        Client client = new ClientBuilder()
                .setAddress(ADDRESS_FOR_TEST)
                .setPersonalNumericalCode(PERSONAL_CODE_FOR_TEST)
                .setIdentityNumeber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setName(NAME_FOR_TEST)
                .build();
        clientService.save(client);
        Client client1 = clientService.findByPersonalCode(PERSONAL_CODE_FOR_TEST).getResult();
        Account account = new AccountBuilder()
                .setAmountOfMoney(MONEY_FOR_TEST)
                .setIdentificationNumber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setType(TYPE_FOR_IDENTIFICATION)
                .setClientId(client.getId())
                .setDateOfCreation(new java.sql.Date(DATE_FOR_TEST.getTime()))
                .build();
        accountService.save(account);
        Assert.assertFalse(accountService.findByIdentificationNumber(account.getIdentificationNumber()).hasErrors());
    }

    @Test
    public void removeAccount() throws EntityNotFoundException{
        accountRepository.removeAll();
        clientRepository.removeAll();
        Client client = new ClientBuilder()
                .setAddress(ADDRESS_FOR_TEST)
                .setPersonalNumericalCode(PERSONAL_CODE_FOR_TEST)
                .setIdentityNumeber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setName(NAME_FOR_TEST)
                .build();
        clientService.save(client);
        Client client1 = clientService.findByPersonalCode(PERSONAL_CODE_FOR_TEST).getResult();
        Account account = new AccountBuilder()
                .setAmountOfMoney(MONEY_FOR_TEST)
                .setIdentificationNumber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setType(TYPE_FOR_IDENTIFICATION)
                .setClientId(client.getId())
                .setDateOfCreation(new java.sql.Date(DATE_FOR_TEST.getTime()))
                .build();
        Account account1 = new AccountBuilder()
                .setAmountOfMoney(MONEY_FOR_TEST)
                .setIdentificationNumber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setType(TYPE_FOR_IDENTIFICATION)
                .setClientId(client.getId())
                .setDateOfCreation(new java.sql.Date(DATE_FOR_TEST.getTime()))
                .build();
        accountService.save(account);
        accountService.save(account1);
        int size = accountService.findAll().size();
        accountService.removeAccount(account);
        Assert.assertTrue(accountService.findAll().size() < size);
    }

}
package service.client;

import database.DBConnectionFactory;
import model.Client;
import model.builder.ClientBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.EntityNotFoundException;
import repository.account.AccountRepository;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepository;


import java.sql.Connection;

public class ClientServiceImplTest {
    public static final String NAME_FOR_TEST = "Mihai";
    public static final String ADDRESS_FOR_TEST = "StrMea";
    public static final Long PERSONAL_CODE_FOR_TEST = 123124L;
    public static final Long IDENTIFICATION_NUMBER_FOR_TEST = 312132L;

    private static ClientRepository clientRepository;
    private static Connection connection;
    private static ClientService clientService;
    private static AccountRepository accountRepository;

    @BeforeClass
    public static void setUp() {
        connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        clientRepository = new ClientRepositoryMySQL(connection);
        clientService = new ClientServiceImpl(clientRepository);
    }

    @Before
    public void cleanUp() {
        clientRepository.removeAll();
    }

    @Test
    public void save(){
        clientRepository.removeAll();
        Client client = new ClientBuilder()
                .setAddress(ADDRESS_FOR_TEST)
                .setPersonalNumericalCode(PERSONAL_CODE_FOR_TEST)
                .setIdentityNumeber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setName(NAME_FOR_TEST)
                .build();
        Assert.assertTrue(!clientService.save(client).hasErrors());
    }

    @Test
    public void update() {
        clientRepository.removeAll();
        Client client = new ClientBuilder()
                .setAddress(ADDRESS_FOR_TEST)
                .setPersonalNumericalCode(PERSONAL_CODE_FOR_TEST)
                .setIdentityNumeber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setName(NAME_FOR_TEST)
                .build();
        clientService.save(client);
        client.setAddress("alabala");
        Assert.assertTrue(!clientService.update(client).hasErrors());
    }

    @Test
    public void remove(){
        clientRepository.removeAll();
        Client client = new ClientBuilder()
                .setAddress(ADDRESS_FOR_TEST)
                .setPersonalNumericalCode(PERSONAL_CODE_FOR_TEST)
                .setIdentityNumeber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setName(NAME_FOR_TEST)
                .build();
        Client client1 = new ClientBuilder()
                .setAddress(ADDRESS_FOR_TEST)
                .setPersonalNumericalCode(312414L)
                .setIdentityNumeber(3211424L)
                .setName("Michael")
                .build();
        clientService.save(client);
        clientService.save(client1);
        int size = clientService.findAll().size();
        clientService.remove(client);
        Assert.assertTrue(clientService.findAll().size() < size);
    }

    @Test
    public void findByPersonalCode() throws EntityNotFoundException {
        clientRepository.removeAll();
        Client client = new ClientBuilder()
                .setAddress(ADDRESS_FOR_TEST)
                .setPersonalNumericalCode(PERSONAL_CODE_FOR_TEST)
                .setIdentityNumeber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setName(NAME_FOR_TEST)
                .build();
        clientService.save(client);
        Assert.assertFalse(clientService.findByPersonalCode(client.getPersonalNumericalCode()).hasErrors());
    }

    @Test
    public void findById() throws EntityNotFoundException {
        clientRepository.removeAll();
        Client client = new ClientBuilder()
                .setAddress(ADDRESS_FOR_TEST)
                .setPersonalNumericalCode(PERSONAL_CODE_FOR_TEST)
                .setIdentityNumeber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setName(NAME_FOR_TEST)
                .build();
        clientService.save(client);
        Assert.assertFalse(clientService.findById(client.getId()).hasErrors());
    }

}


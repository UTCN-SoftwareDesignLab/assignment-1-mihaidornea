package repository.client;

import database.DBConnectionFactory;
import model.Client;
import model.builder.ClientBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.EntityNotFoundException;


import java.sql.Connection;

public class ClientRepositoryMySQLTest {
    public static final String NAME_FOR_TEST = "Mihai";
    public static final String ADDRESS_FOR_TEST ="StrMea";
    public static final Long PERSONAL_CODE_FOR_TEST = 123124L;
    public static final Long IDENTIFICATION_NUMBER_FOR_TEST = 312132L;

    private static ClientRepository clientRepository;
    private static Connection connection;

    @BeforeClass
    public static void setUp(){
        connection = new DBConnectionFactory().getConnectionWrapper().getConnection();
        clientRepository = new ClientRepositoryMySQL(connection);
    }

    @Before
    public void cleanUp(){ clientRepository.removeAll(); }

    @Test
    public void save(){
        clientRepository.removeAll();
        Client client = new ClientBuilder()
                .setAddress(ADDRESS_FOR_TEST)
                .setPersonalNumericalCode(PERSONAL_CODE_FOR_TEST)
                .setIdentityNumeber(IDENTIFICATION_NUMBER_FOR_TEST)
                .setName(NAME_FOR_TEST)
                .build();
        Assert.assertTrue(clientRepository.save(client));
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
        clientRepository.save(client);
        client.setAddress("alabala");
        Assert.assertTrue(clientRepository.update(client));
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
        clientRepository.save(client);
        clientRepository.save(client1);
        int size = clientRepository.findAll().size();
        clientRepository.remove(client);
        Assert.assertTrue(clientRepository.findAll().size() < size);
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
        clientRepository.save(client);
        Assert.assertFalse(clientRepository.findByPersonalCode(client.getPersonalNumericalCode()).hasErrors());
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
        clientRepository.save(client);
        Assert.assertFalse(clientRepository.findById(client.getId()).hasErrors());
    }

}

package service.client;

import model.Client;
import model.User;
import repository.EntityNotFoundException;

import java.util.List;

public interface ClientService {

    List<Client> findAll();

    Client findById(Long Id) throws EntityNotFoundException;

    Client findByPersonalCode(Long personalCode) throws EntityNotFoundException;

    boolean save(Client client);

    boolean update(Client client);

    boolean remove(Client client);



}

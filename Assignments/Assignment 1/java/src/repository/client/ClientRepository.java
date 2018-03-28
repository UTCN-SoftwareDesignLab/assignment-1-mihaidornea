package repository.client;

import model.Client;
import repository.EntityNotFoundException;

import java.util.List;

public interface ClientRepository {

    List<Client> findAll();

    Client findById(Long id) throws EntityNotFoundException;

    Client findByPersonalCode(Long personalCode) throws EntityNotFoundException;

    boolean save(Client client);

    boolean update(Client client);

    boolean remove(Client client);

    void removeAll();

}

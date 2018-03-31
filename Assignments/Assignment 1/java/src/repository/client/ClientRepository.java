package repository.client;

import model.Client;
import model.User;
import model.validation.Notification;
import repository.EntityNotFoundException;

import java.util.List;

public interface ClientRepository {

    List<Client> findAll();

    Notification<Client> findById(Long id) throws EntityNotFoundException;

    Notification<Client> findByPersonalCode(Long personalCode) throws EntityNotFoundException;

    boolean save(Client client);

    boolean update(Client client);

    boolean remove(Client client);

    void removeAll();

}

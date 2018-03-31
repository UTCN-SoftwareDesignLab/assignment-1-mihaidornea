package service.client;

import model.Client;
import model.User;
import model.validation.Notification;
import repository.EntityNotFoundException;

import java.util.List;

public interface ClientService {

    List<Client> findAll();

    Notification<Client> findById(Long Id) throws EntityNotFoundException;

    Notification<Client> findByPersonalCode(Long personalCode) throws EntityNotFoundException;

    Notification<Boolean> save(Client client);

    Notification<Boolean> update(Client client);

    boolean remove(Client client);



}

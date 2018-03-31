package service.client;

import model.Client;
import model.validation.ClientValidator;
import model.validation.Notification;
import model.validation.UserValidator;
import repository.EntityNotFoundException;
import repository.client.ClientRepository;

import java.util.List;

public class ClientServiceImpl implements ClientService{

    private ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository){this.clientRepository=clientRepository;}

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Notification<Client> findById(Long id) throws EntityNotFoundException {
        return clientRepository.findById(id);
    }

    @Override
    public Notification<Client> findByPersonalCode(Long personalCode) throws EntityNotFoundException {
        return clientRepository.findByPersonalCode(personalCode);
    }

    @Override
    public Notification<Boolean> save(Client client) {
        ClientValidator userValidator = new ClientValidator(client);
        boolean goodClient = userValidator.validate();
        Notification<Boolean> notification = new Notification<>();
        if (!goodClient) {
            userValidator.getErrors().forEach(notification::addError);
            notification.setResult(Boolean.FALSE);
        } else {
            notification.setResult(clientRepository.save(client));
        }
        return notification;
    }

    @Override
    public Notification<Boolean> update(Client client) {

        Notification<Boolean> notification = new Notification<>();
        ClientValidator validator = new ClientValidator(client);
        boolean good = validator.validate();

        if (!good){
            validator.getErrors().forEach(notification::addError);
            return notification;
        } else {
            notification.setResult(clientRepository.update(client));
            return notification;
        }
    }

    @Override
    public boolean remove(Client client) {
        return clientRepository.remove(client);
    }
}

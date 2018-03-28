package service.client;

import model.Client;
import repository.EntityNotFoundException;
import repository.client.ClientRepository;
import repository.user.UserRepository;

import java.util.List;

public class ClientServiceImpl implements ClientService{

    private ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository){this.clientRepository=clientRepository;}

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) throws EntityNotFoundException {
        return clientRepository.findById(id);
    }

    @Override
    public Client findByPersonalCode(Long personalCode) throws EntityNotFoundException {
        return clientRepository.findByPersonalCode(personalCode);
    }

    @Override
    public boolean save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public boolean update(Client client) {
        return clientRepository.update(client);
    }

    @Override
    public boolean remove(Client client) {
        return clientRepository.remove(client);
    }
}

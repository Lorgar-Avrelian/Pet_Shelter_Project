package sky.pro.Animals.service;

import org.springframework.stereotype.Service;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.repository.ClientRepository;

import java.util.Collection;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Collection<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client getById(Long id) {
        return clientRepository.findById(id).get();
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client delete(Long id) {
        Client client = clientRepository.getById(id);
        clientRepository.delete(client);
        return client;
    }
}

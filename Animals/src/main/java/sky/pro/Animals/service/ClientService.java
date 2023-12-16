package sky.pro.Animals.service;

import sky.pro.Animals.entity.Client;

import java.sql.Date;
import java.util.Collection;

public interface ClientService {
    Collection<Client> getAll();

    Client getById(Long id);

    Client save(Client client);

    Client save(Long id,
                String firstName,
                String lastName,
                String userName,
                String address,
                Date birthday,
                String passport,
                Long chatId,
                Long firstPetId,
                Long secondPetId,
                Long thirdPetId);

    Client delete(Long id);

    Client getByChatId(Long chatId);
}

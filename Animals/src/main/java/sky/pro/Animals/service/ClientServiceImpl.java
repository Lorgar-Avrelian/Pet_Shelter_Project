package sky.pro.Animals.service;

import lombok.extern.log4j.Log4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.repository.ClientRepository;

import java.sql.Date;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Service for working with clients data
 * <p>
 * <hr>
 * <p>
 * Сервис для работы с данными клиентов
 */
@Service
@Log4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final PetServiceImpl petService;

    public ClientServiceImpl(ClientRepository clientRepository, PetServiceImpl petService) {
        this.clientRepository = clientRepository;
        this.petService = petService;
    }

    /**
     * Method for getting collection with all clients. <br>
     * Used repository method {@link JpaRepository#findAll()}
     * <hr>
     * Метод для получения коллекции, содержащей всех клиентов. <br>
     * Используется метод репозитория {@link JpaRepository#findAll()}
     * <hr>
     *
     * @return Collection with all clients / Коллекцию со всеми клиентами
     * @see JpaRepository#findAll()
     */
    @Override
    public Collection<Client> getAll() {
        return clientRepository.findAll();
    }

    /**
     * Method for getting client with this id. <br>
     * Used repository method {@link JpaRepository#findById(Object)}
     * <hr>
     * Метод для получения клиента, имеющего данный id. <br>
     * Используется метод репозитория {@link JpaRepository#findById(Object)}
     * <hr>
     *
     * @param id of client / id клиента
     * @return Client with this id / Клиента с данным id
     * @see JpaRepository#findById(Object)
     */
    @Override
    public Client getById(Long id) {
        try {
            return clientRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Method for saving client in DB when goes edit. <br>
     * Used repository method {@link JpaRepository#save(Object)}
     * <hr>
     * Метод для сохранения клиента в БД при редактировании. <br>
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * <hr>
     *
     * @param client / клиент
     * @return saved client / сохраненного клиента
     * @see JpaRepository#save(Object)
     */
    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    /**
     * Method for saving new client in DB. <br>
     * Used repository method {@link JpaRepository#save(Object)}
     * <hr>
     * Метод для сохранения нового клиента в БД. <br>
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * <hr>
     *
     * @param firstName
     * @param lastName
     * @param userName
     * @param address
     * @param birthday
     * @param passport
     * @param chatId
     * @param firstPetId
     * @param secondPetId
     * @param thirdPetId
     * @return saved client / сохраненного клиента
     * @see JpaRepository#save(Object)
     */
    @Override
    public Client save(Long id,
                       String firstName,
                       String lastName,
                       String userName,
                       String address,
                       Date birthday,
                       String passport,
                       Long chatId,
                       Long firstPetId,
                       Long secondPetId,
                       Long thirdPetId) {
        Client client = new Client();
        client.setId(0L);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setUserName(userName);
        client.setAddress(address);
        client.setBirthday(birthday);
        client.setPassport(passport);
        client.setChatId(chatId);
        Collection<Pet> clientPets = null;
        if (firstPetId != null) {
            clientPets.add(petService.getById(firstPetId));
        }
        if (secondPetId != null) {
            clientPets.add(petService.getById(secondPetId));
        }
        if (thirdPetId != null) {
            clientPets.add(petService.getById(thirdPetId));
        }
        client.setPets(clientPets);
        return clientRepository.save(client);
    }

    /**
     * Method for deleting client with this id from DB. <br>
     * Used repository method {@link JpaRepository#delete(Object)} <br>
     * Also used repository method {@link JpaRepository#findById(Object)}
     * <hr>
     * Метод для удаления клиента с данным id из БД. <br>
     * Используется метод репозитория {@link JpaRepository#delete(Object)} <br>
     * Также используется метод репозитория {@link JpaRepository#findById(Object)}
     * <hr>
     *
     * @param id of client / id клиента
     * @return deleted client / удаленного клиента
     * @see JpaRepository#delete(Object)
     * @see JpaRepository#findById(Object)
     */
    @Override
    public Client delete(Long id) {
        Client client = clientRepository.getById(id);
        clientRepository.delete(client);
        return client;
    }

    /**
     * Method for searching client with this chatId from DB. <br>
     * Used repository method {@link ClientRepository#findByChatId(Long)} <br>
     * <hr>
     * Метод для поиска в БД клиента с данным chatId. <br>
     * Используется метод репозитория {@link ClientRepository#findByChatId(Long)} <br>
     * <hr>
     *
     * @param chatId
     * @return Client or null / Клиента или null
     */
    @Override
    public Client getByChatId(Long chatId) {
        try {
            return clientRepository.findByChatId(chatId).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}

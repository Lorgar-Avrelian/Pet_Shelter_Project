package sky.pro.Animals.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.model.PetVariety;
import sky.pro.Animals.repository.ClientRepository;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    Long clientId = 1L;
    Long chatId = 111l;

    Pet pet1 = new Pet(1l, "Name1", new Date(2023, 10, 10), true
            , PetVariety.cat, null);

    List<Pet> pets = List.of(pet1);

    Client client1 = new Client(1l, "Fn", "Ln", "Un", "addres"
            , new Date(2000, 11, 11), "123321", 111l, pets);
    List<Client> expected = List.of(client1);
    //данный метод не сработал как надо ,поэтому вернул в первоначальный вид
//    @BeforeEach
//    void initTest() throws Exception {
//        when(clientRepository.findAll()).thenReturn(expected);
//        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client1));
//
//        when(clientRepository.save(client1)).thenReturn(client1);
//        when(clientRepository.getById(clientId)).thenReturn(client1);
//
//        when(clientRepository.findByChatId(chatId)).thenReturn(Optional.of(client1));
//
//    }

    @Test
    void getAll() {



        when(clientRepository.findAll()).thenReturn(expected);
// Calling the service method
        List<Client> actual = (List<Client>) clientService.getAll();

        // Assertions
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    void getById() {


        // Mocking the repository behavior
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client1));

        // Calling the service method
        Client actual = clientService.getById(clientId);

        // Assertions
        assertNotNull(actual);
        assertEquals(client1, actual);
    }

    @Test
    void save() {
        // Mocking the repository behavior
        when(clientRepository.save(client1)).thenReturn(client1);

        // Calling the service method
        Client actual = clientService.save(client1);

        // Assertions
        assertNotNull(actual);
        assertEquals(client1, actual);
    }

    @Test
    void delete() {

        // Mocking the repository behavior
        when(clientRepository.getById(clientId)).thenReturn(client1);

        // Calling the service method
        Client actual = clientService.delete(1l);

        // Assertions
        assertNotNull(actual);
        assertEquals(client1, actual);


    }

    @Test
    void getByChatId() {
        // Mocking the repository behavior
        when(clientRepository.findByChatId(chatId)).thenReturn(Optional.of(client1));

        // Calling the service method
        Client actual = clientService.getByChatId(chatId);

        // Assertions
        assertNotNull(actual);
        assertEquals(client1, actual);
    }
}







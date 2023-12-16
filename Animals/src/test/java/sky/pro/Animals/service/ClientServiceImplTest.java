package sky.pro.Animals.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.repository.ClientRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static sky.pro.Animals.constants.Constants.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @Mock
    ClientRepository clientRepository;
    @InjectMocks
    ClientServiceImpl clientService;

    @BeforeEach
    void init() {
        lenient().when(clientRepository.findAll()).thenReturn(CLIENTS);
        lenient().when(clientRepository.findById(anyLong())).thenReturn(Optional.of(CLIENT_5));
        lenient().when(clientRepository.getById(anyLong())).thenReturn(CLIENT_5);
        lenient().when(clientRepository.save(any(Client.class))).thenReturn(CLIENT_1);
        lenient().doNothing().when(clientRepository).delete(any(Client.class));
        lenient().when(clientRepository.findByChatId(anyLong())).thenReturn(Optional.of(CLIENT_2));
    }

    @Test
    void getAll() {
        assertEquals(CLIENTS, clientService.getAll());
    }

    @Test
    void getById() {
        assertEquals(CLIENT_5, clientService.getById(CLIENT_5.getId()));
    }

    @Test
    void save() {
        assertEquals(CLIENT_1, clientService.save(CLIENT_1));
    }

    @Test
    void testSave() {
        assertEquals(CLIENT_1, clientService.save(CLIENT_1.getId(), CLIENT_1.getFirstName(), CLIENT_1.getLastName(), CLIENT_1.getUserName(), CLIENT_1.getAddress(), CLIENT_1.getBirthday(), CLIENT_1.getPassport(), CLIENT_1.getChatId(), null, null, null));
    }

    @Test
    void delete() {
        verify(clientRepository, times(0)).delete(CLIENT_2);
        assertEquals(CLIENT_5, clientService.delete(CLIENT_5.getId()));
    }

    @Test
    void getByChatId() {
        assertEquals(CLIENT_2, clientService.getByChatId(CLIENT_2.getChatId()));
    }
}
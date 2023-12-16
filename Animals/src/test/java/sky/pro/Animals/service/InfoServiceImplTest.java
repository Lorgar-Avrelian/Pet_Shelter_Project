package sky.pro.Animals.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.Animals.entity.Info;
import sky.pro.Animals.repository.InfoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static sky.pro.Animals.constants.Constants.INFO_1;
import static sky.pro.Animals.constants.Constants.INFO_LIST;

@ExtendWith(MockitoExtension.class)
class InfoServiceImplTest {
    @Mock
    InfoRepository infoRepository;
    @InjectMocks
    InfoServiceImpl infoService;

    @BeforeEach
    void init() {
        lenient().when(infoRepository.save(any(Info.class))).thenReturn(INFO_1);
        lenient().when(infoRepository.findAll()).thenReturn(INFO_LIST);
        lenient().when(infoRepository.findById(anyLong())).thenReturn(Optional.of(INFO_1));
    }

    @Test
    void checkInfo() {
        infoService.checkInfo();
        verify(infoRepository, times(1)).findAll();
        verify(infoRepository, times(20)).save(any(Info.class));
    }

    @Test
    void editInfo() {
        assertEquals(INFO_1.getKey() + " изменен(а)", infoService.editInfo(INFO_1));
    }

    @Test
    void getAllInfo() {
        assertIterableEquals(INFO_LIST, infoService.getAllInfo());
    }

    @Test
    void getInfoTextById() {
        assertEquals(INFO_1.getText(), infoService.getInfoTextById(INFO_1.getId()));
    }

    @Test
    void getInfoKeyById() {
        assertEquals(INFO_1.getKey(), infoService.getInfoKeyById(INFO_1.getId()));
    }

    @Test
    void getById() {
        assertEquals(INFO_1, infoService.getById(INFO_1.getId()));
    }
}
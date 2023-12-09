package sky.pro.Animals.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.Animals.repository.InfoRepository;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InfoServiceImplTest {
    @InjectMocks
    private InfoServiceImpl infoService;
    @Mock
    private InfoRepository infoRepository;
//    @Test
//    void checkInfo() {
//        // Arrange
//        when(infoRepository.findAll()).thenReturn(new ArrayList<>());
//
//        // Act
//        infoService.checkInfo();
//
//        // Assert
//        verify(infoRepository, times(20)).save(any(Info.class));
//    }

    @Test
    void editInfo() {
    }

    @Test
    void getAllInfo() {
    }

    @Test
    void getInfoTextById() {
    }
}
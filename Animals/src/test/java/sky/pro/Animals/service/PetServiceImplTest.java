package sky.pro.Animals.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.model.PetVariety;
import sky.pro.Animals.repository.PetRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static sky.pro.Animals.constants.Constants.*;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {
    @Mock
    PetRepository petRepository;
    @InjectMocks
    PetServiceImpl petService;

    @BeforeEach
    void init() {
        lenient().when(petRepository.findAll()).thenReturn(PETS);
        lenient().when(petRepository.findById(anyLong())).thenReturn(Optional.of(CAT_1));
        lenient().when(petRepository.save(any(Pet.class))).thenReturn(CAT_1);
        lenient().when(petRepository.getById(anyLong())).thenReturn(CAT_1);
        lenient().doNothing().when(petRepository).delete(any(Pet.class));
        lenient().when(petRepository.findAllByPetVariety(any(PetVariety.class))).thenReturn(CATS);
    }

    @Test
    void getAll() {
        assertEquals(PETS, petService.getAll());
    }

    @Test
    void getById() {
        assertEquals(CAT_1, petService.getById(CAT_1.getId()));
    }

    @Test
    void save() {
        assertEquals(CAT_1, petService.save(CAT_1));
    }

    @Test
    void testSave() {
        assertEquals(CAT_1, petService.save(CAT_1.getId(), CAT_1.getName(), CAT_1.getBirthday(), CAT_1.isAlive(), CAT_1.getPetVariety(), null));
    }

    @Test
    void delete() {
        verify(petRepository, times(0)).delete(CAT_1);
        assertEquals(CAT_1, petService.delete(CAT_1.getId()));
    }

    @Test
    void getPetListByVariety() {
        assertIterableEquals(CATS, petService.getPetListByVariety(PetVariety.cat));
    }
}
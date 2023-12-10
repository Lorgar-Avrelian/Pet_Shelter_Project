package sky.pro.Animals.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.model.PetVariety;
import sky.pro.Animals.repository.PetRepository;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {
    @InjectMocks
    private PetServiceImpl petService;
    @Mock
    private PetRepository petRepository;
    @Test
    void getAll() {
        // Arrange
        Pet pet1 = new Pet(1L, "Fluffy", new Date(2023-11-11), true, PetVariety.cat, null);
        Pet pet2 = new Pet(2L, "Buddy", new Date(2023-11-11), true, PetVariety.DOG, null);
        Collection<Pet> mockPetList = Arrays.asList(pet1, pet2);

        // Mocking the behavior of the petRepository
        Mockito.when(petRepository.findAll()).thenReturn((List<Pet>) mockPetList);

        // Act
        Collection<Pet> result = petService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pet1));
        assertTrue(result.contains(pet2));
    }

    @Test
    void getById() {
        // Arrange
        Pet pet = new Pet(1L, "Fluffy", new Date(2023-10-10), true, PetVariety.dog, null);

        // Mocking the behavior of the petRepository
        Mockito.when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        // Act
        Pet result = petService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(pet, result);
    }

    @Test
    void save() {
        // Arrange
        Pet pet = new Pet(1L, "Fluffy", new Date(2023-11-11), true, PetVariety.DOG, null);

        // Mocking the behavior of the petRepository
        Mockito.when(petRepository.save(pet)).thenReturn(pet);

        // Act
        Pet result = petService.save(pet);

        // Assert
        assertNotNull(result);
        assertEquals(pet, result);
    }

    @Test
    void delete() {
        // Arrange
        Pet pet = new Pet(1L, "Fluffy", new Date(200-11-11), true, PetVariety.cat, null);

        // Mocking the behavior of the petRepository
        Mockito.when(petRepository.getById(1L)).thenReturn(pet);

        // Act
        Pet result = petService.delete(1L);

        // Assert
        assertNotNull(result);
        assertEquals(pet, result);
    }

    @Test
    void getPetListByVariety() {
        // Arrange
        List<Pet> mockPetList = Arrays.asList(
                new Pet(1L, "Fluffy", new Date(2023-10-10), true, PetVariety.cat, null),
                new Pet(2L, "Buddy", new Date(2023-11-11), true, PetVariety.cat, null)
        );

        // Mocking the behavior of the petRepository
        Mockito.when(petRepository.findAllByPetVariety(PetVariety.cat)).thenReturn(mockPetList);

        // Act
        List <Pet> result = petService.getPetListByVariety(PetVariety.cat);

        // Assert
        assertNotNull(result);
        assertEquals(mockPetList,result);

    }
}
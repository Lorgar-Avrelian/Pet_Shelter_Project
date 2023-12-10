package sky.pro.Animals.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.Animals.entity.Volunteer;
import sky.pro.Animals.repository.VolunteerRepository;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceImplTest {
    @InjectMocks
    private VolunteerServiceImpl volunteerService;
    @Mock
    private VolunteerRepository volunteerRepository;


    @Test
    void getAll() {

            // Arrange
            Volunteer volunteer1 = new Volunteer(1L, "John Doe", "Address 1", new Date(2000-11-11), "AB123456", 111l, "Position 1");
            Volunteer volunteer2 = new Volunteer(2L, "Jane Doe", "Address 2", new Date(2000-10-10), "CD789012", 222l, "Position 2");
            when(volunteerRepository.findAll()).thenReturn(Arrays.asList(volunteer1, volunteer2));

            // Act
            Collection<Volunteer> result = volunteerService.getAll();

            // Assert
            assertEquals(2, result.size());
            assertTrue(result.contains(volunteer1));
            assertTrue(result.contains(volunteer2));

    }

    @Test
    void getById() {
        // Arrange
        Long volunteerId = 1L;
        Volunteer expectedVolunteer = new Volunteer(volunteerId, "John Doe", "Address", new Date(2000-11-11), "AB123456", 111l, "Position");
        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(expectedVolunteer));

        // Act
        Volunteer result = volunteerService.getById(volunteerId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedVolunteer, result);
    }

    @Test
    void save() {
        // Arrange
        Volunteer volunteerToSave = new Volunteer(1L, "John Doe", "Address", new Date(2000-11-11), "AB123456", 123l, "Position");
        Volunteer savedVolunteer = new Volunteer(1L, "John Doe", "Address", new Date(2000-11-11), "AB123456", 123l, "Position");
        when(volunteerRepository.save(volunteerToSave)).thenReturn(savedVolunteer);

        // Act
        Volunteer result = volunteerService.save(volunteerToSave);

        // Assert
        assertNotNull(result);
        assertEquals(savedVolunteer, result);
    }
    @Test
    void delete() {
        // Arrange
        Long volunteerId = 1L;
        Volunteer volunteerToDelete = new Volunteer(volunteerId, "John Doe", "Address", new Date(2000-11-11), "AB123456", 123l, "Position");
        when(volunteerRepository.getById(volunteerId)).thenReturn(volunteerToDelete);

        // Act
        Volunteer result = volunteerService.delete(volunteerId);

        // Assert
        assertNotNull(result);
        assertEquals(volunteerToDelete, result);
        verify(volunteerRepository, times(1)).delete(volunteerToDelete);
    }
}



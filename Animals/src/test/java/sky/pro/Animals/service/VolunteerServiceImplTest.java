package sky.pro.Animals.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.Animals.entity.Volunteer;
import sky.pro.Animals.repository.VolunteerRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static sky.pro.Animals.constants.Constants.VOLUNTEERS;
import static sky.pro.Animals.constants.Constants.VOLUNTEER_1;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceImplTest {
    @Mock
    VolunteerRepository volunteerRepository;
    @InjectMocks
    VolunteerServiceImpl volunteerService;

    @BeforeEach
    void init() {
        lenient().when(volunteerRepository.findAll()).thenReturn(VOLUNTEERS);
        lenient().when(volunteerRepository.findById(anyLong())).thenReturn(Optional.of(VOLUNTEER_1));
        lenient().when(volunteerRepository.save(any(Volunteer.class))).thenReturn(VOLUNTEER_1);
        lenient().doNothing().when(volunteerRepository).delete(any(Volunteer.class));
        lenient().when(volunteerRepository.getById(anyLong())).thenReturn(VOLUNTEER_1);
    }

    @Test
    void getAll() {
        assertEquals(VOLUNTEERS, volunteerService.getAll());
    }

    @Test
    void getById() {
        assertEquals(VOLUNTEER_1, volunteerService.getById(VOLUNTEER_1.getId()));
    }

    @Test
    void save() {
        assertEquals(VOLUNTEER_1, volunteerService.save(VOLUNTEER_1));
    }

    @Test
    void testSave() {
        assertEquals(VOLUNTEER_1, volunteerService.save(VOLUNTEER_1.getId(), VOLUNTEER_1.getFio(), VOLUNTEER_1.getAddress(), VOLUNTEER_1.getBirthday(), VOLUNTEER_1.getPassport(), VOLUNTEER_1.getChatId(), VOLUNTEER_1.getWorkPosition()));
    }

    @Test
    void delete() {
        verify(volunteerRepository, times(0)).delete(VOLUNTEER_1);
        assertEquals(VOLUNTEER_1, volunteerService.delete(VOLUNTEER_1.getId()));
    }
}
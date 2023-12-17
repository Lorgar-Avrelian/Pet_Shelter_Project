package sky.pro.Animals.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.Animals.entity.ProbationPeriod;
import sky.pro.Animals.repository.ProbationPeriodRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static sky.pro.Animals.constants.Constants.PERIODS;
import static sky.pro.Animals.constants.Constants.PERIOD_1;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceImplTest {
    @Mock
    ProbationPeriodRepository probation;
    @InjectMocks
    SchedulerServiceImpl schedulerService;

    @BeforeEach
    void init() {
        lenient().when(probation.findAll()).thenReturn(PERIODS);
        lenient().when(probation.save(any(ProbationPeriod.class))).thenReturn(PERIOD_1);
        lenient().doNothing().when(probation).delete(any(ProbationPeriod.class));
        lenient().when(probation.findById(anyLong())).thenReturn(Optional.of(PERIOD_1));
    }

    @Test
    void getProbation() {
        assertIterableEquals(PERIODS, schedulerService.getProbation());
    }

    @Test
    void updateProbation() {
        schedulerService.getProbation();
        verify(probation, times(1)).findAll();
        verify(probation, times(0)).save(any(ProbationPeriod.class));
    }

    @Test
    void deleteProbation() {
        schedulerService.deleteProbation(PERIOD_1.getId());
        verify(probation, times(1)).findById(anyLong());
    }
}
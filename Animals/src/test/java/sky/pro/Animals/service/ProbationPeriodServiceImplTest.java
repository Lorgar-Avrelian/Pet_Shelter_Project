package sky.pro.Animals.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import sky.pro.Animals.entity.ProbationPeriod;
import sky.pro.Animals.listener.PetShelterTelegramBot;
import sky.pro.Animals.repository.ProbationPeriodRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static sky.pro.Animals.constants.Constants.CLIENT_5;
import static sky.pro.Animals.constants.Constants.PERIOD_1;

@ExtendWith(MockitoExtension.class)
class ProbationPeriodServiceImplTest {
    @Mock
    ProbationPeriodRepository probationPeriodRepository;
    @Mock
    PetShelterTelegramBot telegramBot;
    @InjectMocks
    ProbationPeriodServiceImpl probationPeriodService;

    @Test
    void changeLastDay() {
        when(probationPeriodRepository.findByClientId(anyLong())).thenReturn(PERIOD_1);
        when(probationPeriodRepository.save(any(ProbationPeriod.class))).thenReturn(PERIOD_1);
        doNothing().when(telegramBot).exec(any(SendMessage.class));
        assertEquals(CLIENT_5, probationPeriodService.changeLastDay(CLIENT_5, 10));
    }
}
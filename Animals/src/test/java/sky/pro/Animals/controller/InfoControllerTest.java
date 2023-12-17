package sky.pro.Animals.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sky.pro.Animals.configuration.CacheConfig;
import sky.pro.Animals.configuration.PetShelterTelegramConfig;
import sky.pro.Animals.entity.Info;
import sky.pro.Animals.listener.PetShelterTelegramBot;
import sky.pro.Animals.repository.*;
import sky.pro.Animals.service.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sky.pro.Animals.constants.Constants.*;
import static sky.pro.Animals.constants.Constants.CLIENT_5;

@WebMvcTest
class InfoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CacheConfig cacheConfig;
    @MockBean
    private PetShelterTelegramConfig petShelterTelegramConfig;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private PetAvatarRepository petAvatarRepository;
    @MockBean
    private PetRepository petRepository;
    @MockBean
    private ProbationPeriodRepository probationPeriodRepository;
    @MockBean
    private InfoRepository infoRepository;
    @MockBean
    private VolunteerRepository volunteerRepository;
    @MockBean
    private DailyReportRepository dailyReportRepository;
    @SpyBean
    private ClientServiceImpl clientService;
    @SpyBean
    private InfoServiceImpl infoService;
    @SpyBean
    private PetAvatarServiceImpl petAvatarService;
    @SpyBean
    private PetServiceImpl petService;
    @SpyBean
    private ProbationPeriodServiceImpl probationPeriodService;
    @SpyBean
    private DailyReportServiceImpl dailyReportService;
    @SpyBean
    private VolunteerServiceImpl volunteerService;
    @SpyBean
    private SchedulerServiceImpl schedulerService;
    @SpyBean
    private PetShelterTelegramBot telegramBot;
    @InjectMocks
    private InfoController infoController;

    @BeforeEach
    void init() {
        lenient().when(infoRepository.findAll()).thenReturn(INFO_LIST);
        lenient().when(infoRepository.findById(anyLong())).thenReturn(Optional.of(INFO_1));
        lenient().when(infoRepository.save(any(Info.class))).thenReturn(INFO_1);
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get("/info")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(INFO_1.getId()))
               .andExpect(jsonPath("$[0].key").value(INFO_1.getKey()))
               .andExpect(jsonPath("$[0].text").value(INFO_1.getText()))
               .andExpect(jsonPath("$[1].id").value(INFO_2.getId()))
               .andExpect(jsonPath("$[1].key").value(INFO_2.getKey()))
               .andExpect(jsonPath("$[1].text").value(INFO_2.getText()))
               .andExpect(jsonPath("$[2].id").value(INFO_3.getId()))
               .andExpect(jsonPath("$[2].key").value(INFO_3.getKey()))
               .andExpect(jsonPath("$[2].text").value(INFO_3.getText()))
               .andExpect(jsonPath("$[3].id").value(INFO_4.getId()))
               .andExpect(jsonPath("$[3].key").value(INFO_4.getKey()))
               .andExpect(jsonPath("$[3].text").value(INFO_4.getText()))
               .andExpect(jsonPath("$[4].id").value(INFO_5.getId()))
               .andExpect(jsonPath("$[4].key").value(INFO_5.getKey()))
               .andExpect(jsonPath("$[4].text").value(INFO_5.getText()));
    }

    @Test
    void editInfo() throws Exception {
        mockMvc.perform(post("/info/edit?id=" + INFO_1.getId() + "&editedText=" + INFO_1.getText())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").value(INFO_1.getKey() + " изменен(а)"));
    }
}
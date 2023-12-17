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
import sky.pro.Animals.entity.Volunteer;
import sky.pro.Animals.listener.PetShelterTelegramBot;
import sky.pro.Animals.repository.*;
import sky.pro.Animals.service.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sky.pro.Animals.constants.Constants.*;

@WebMvcTest
class VolunteerControllerTest {
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
    private VolunteerController volunteerController;

    @BeforeEach
    void init() {
        lenient().doNothing().when(infoService).checkInfo();
        lenient().when(volunteerRepository.findAll()).thenReturn(VOLUNTEERS);
        lenient().when(volunteerRepository.findById(anyLong())).thenReturn(Optional.of(VOLUNTEER_1));
        lenient().when(volunteerRepository.getById(anyLong())).thenReturn(VOLUNTEER_1);
        lenient().when(volunteerRepository.save(any(Volunteer.class))).thenReturn(VOLUNTEER_1);
    }

    @Test
    void getAllVolunteers() throws Exception {
        mockMvc.perform(get("/volunteer/get")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(VOLUNTEER_1.getId()))
               .andExpect(jsonPath("$[0].fio").value(VOLUNTEER_1.getFio()))
               .andExpect(jsonPath("$[0].address").value(VOLUNTEER_1.getAddress()))
               .andExpect(jsonPath("$[0].birthday").value(String.valueOf(VOLUNTEER_1.getBirthday())))
               .andExpect(jsonPath("$[0].passport").value(VOLUNTEER_1.getPassport()))
               .andExpect(jsonPath("$[0].chatId").value(VOLUNTEER_1.getChatId()))
               .andExpect(jsonPath("$[0].workPosition").value(VOLUNTEER_1.getWorkPosition()))
               .andExpect(jsonPath("$[1].id").value(VOLUNTEER_2.getId()))
               .andExpect(jsonPath("$[1].fio").value(VOLUNTEER_2.getFio()))
               .andExpect(jsonPath("$[1].address").value(VOLUNTEER_2.getAddress()))
               .andExpect(jsonPath("$[1].birthday").value(String.valueOf(VOLUNTEER_2.getBirthday())))
               .andExpect(jsonPath("$[1].passport").value(VOLUNTEER_2.getPassport()))
               .andExpect(jsonPath("$[1].chatId").value(VOLUNTEER_2.getChatId()))
               .andExpect(jsonPath("$[1].workPosition").value(VOLUNTEER_2.getWorkPosition()))
               .andExpect(jsonPath("$[2].id").value(VOLUNTEER_3.getId()))
               .andExpect(jsonPath("$[2].fio").value(VOLUNTEER_3.getFio()))
               .andExpect(jsonPath("$[2].address").value(VOLUNTEER_3.getAddress()))
               .andExpect(jsonPath("$[2].birthday").value(String.valueOf(VOLUNTEER_3.getBirthday())))
               .andExpect(jsonPath("$[2].passport").value(VOLUNTEER_3.getPassport()))
               .andExpect(jsonPath("$[2].chatId").value(VOLUNTEER_3.getChatId()))
               .andExpect(jsonPath("$[2].workPosition").value(VOLUNTEER_3.getWorkPosition()))
               .andExpect(jsonPath("$[3].id").value(VOLUNTEER_4.getId()))
               .andExpect(jsonPath("$[3].fio").value(VOLUNTEER_4.getFio()))
               .andExpect(jsonPath("$[3].address").value(VOLUNTEER_4.getAddress()))
               .andExpect(jsonPath("$[3].birthday").value(String.valueOf(VOLUNTEER_4.getBirthday())))
               .andExpect(jsonPath("$[3].passport").value(VOLUNTEER_4.getPassport()))
               .andExpect(jsonPath("$[3].chatId").value(VOLUNTEER_4.getChatId()))
               .andExpect(jsonPath("$[3].workPosition").value(VOLUNTEER_4.getWorkPosition()))
               .andExpect(jsonPath("$[4].id").value(VOLUNTEER_5.getId()))
               .andExpect(jsonPath("$[4].fio").value(VOLUNTEER_5.getFio()))
               .andExpect(jsonPath("$[4].address").value(VOLUNTEER_5.getAddress()))
               .andExpect(jsonPath("$[4].birthday").value(String.valueOf(VOLUNTEER_5.getBirthday())))
               .andExpect(jsonPath("$[4].passport").value(VOLUNTEER_5.getPassport()))
               .andExpect(jsonPath("$[4].chatId").value(VOLUNTEER_5.getChatId()))
               .andExpect(jsonPath("$[4].workPosition").value(VOLUNTEER_5.getWorkPosition()));
    }

    @Test
    void getVolunteer() throws Exception {
        mockMvc.perform(get("/volunteer/get/1")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(VOLUNTEER_1.getId()))
               .andExpect(jsonPath("$.fio").value(VOLUNTEER_1.getFio()))
               .andExpect(jsonPath("$.address").value(VOLUNTEER_1.getAddress()))
               .andExpect(jsonPath("$.birthday").value(String.valueOf(VOLUNTEER_1.getBirthday())))
               .andExpect(jsonPath("$.passport").value(VOLUNTEER_1.getPassport()))
               .andExpect(jsonPath("$.chatId").value(VOLUNTEER_1.getChatId()))
               .andExpect(jsonPath("$.workPosition").value(VOLUNTEER_1.getWorkPosition()));
    }

    @Test
    void writeVolunteer() throws Exception {
        mockMvc.perform(post("/volunteer/write?fio=" + VOLUNTEER_1.getFio() + "&address=" + VOLUNTEER_1.getAddress() + "&birthday=" + VOLUNTEER_1.getBirthday() + "&passport=" + VOLUNTEER_1.getPassport() + "&chatId=" + VOLUNTEER_1.getChatId() + "&workPosition=" + VOLUNTEER_1.getWorkPosition())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(VOLUNTEER_1.getId()))
               .andExpect(jsonPath("$.fio").value(VOLUNTEER_1.getFio()))
               .andExpect(jsonPath("$.address").value(VOLUNTEER_1.getAddress()))
               .andExpect(jsonPath("$.birthday").value(String.valueOf(VOLUNTEER_1.getBirthday())))
               .andExpect(jsonPath("$.passport").value(VOLUNTEER_1.getPassport()))
               .andExpect(jsonPath("$.chatId").value(VOLUNTEER_1.getChatId()))
               .andExpect(jsonPath("$.workPosition").value(VOLUNTEER_1.getWorkPosition()));
    }

    @Test
    void editVolunteer() throws Exception {
        mockMvc.perform(put("/volunteer/edit?id=" + VOLUNTEER_1.getId() + "&fio=" + VOLUNTEER_1.getFio() + "&address=" + VOLUNTEER_1.getAddress() + "&birthday=" + VOLUNTEER_1.getBirthday() + "&passport=" + VOLUNTEER_1.getPassport() + "&chatId=" + VOLUNTEER_1.getChatId() + "&workPosition=" + VOLUNTEER_1.getWorkPosition())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(VOLUNTEER_1.getId()))
               .andExpect(jsonPath("$.fio").value(VOLUNTEER_1.getFio()))
               .andExpect(jsonPath("$.address").value(VOLUNTEER_1.getAddress()))
               .andExpect(jsonPath("$.birthday").value(String.valueOf(VOLUNTEER_1.getBirthday())))
               .andExpect(jsonPath("$.passport").value(VOLUNTEER_1.getPassport()))
               .andExpect(jsonPath("$.chatId").value(VOLUNTEER_1.getChatId()))
               .andExpect(jsonPath("$.workPosition").value(VOLUNTEER_1.getWorkPosition()));
    }

    @Test
    void deleteVolunteer() throws Exception {
        mockMvc.perform(delete("/volunteer/delete/1?id=1")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(VOLUNTEER_1.getId()))
               .andExpect(jsonPath("$.fio").value(VOLUNTEER_1.getFio()))
               .andExpect(jsonPath("$.address").value(VOLUNTEER_1.getAddress()))
               .andExpect(jsonPath("$.birthday").value(String.valueOf(VOLUNTEER_1.getBirthday())))
               .andExpect(jsonPath("$.passport").value(VOLUNTEER_1.getPassport()))
               .andExpect(jsonPath("$.chatId").value(VOLUNTEER_1.getChatId()))
               .andExpect(jsonPath("$.workPosition").value(VOLUNTEER_1.getWorkPosition()));
    }
}
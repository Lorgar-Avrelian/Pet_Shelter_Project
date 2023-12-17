package sky.pro.Animals.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import sky.pro.Animals.configuration.CacheConfig;
import sky.pro.Animals.configuration.PetShelterTelegramConfig;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.entity.ProbationPeriod;
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
@AutoConfigureMockMvc
class ClientControllerTest {
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
    private ClientController clientController;

    @BeforeEach
    void init() {
        lenient().doNothing().when(infoService).checkInfo();
        lenient().when(clientRepository.findAll()).thenReturn(CLIENTS);
        lenient().when(clientRepository.findById(anyLong())).thenReturn(Optional.of(CLIENT_5));
        lenient().when(clientRepository.getById(anyLong())).thenReturn(CLIENT_5);
        lenient().when(clientRepository.save(any(Client.class))).thenReturn(CLIENT_5);
        lenient().doNothing().when(clientRepository).delete(any(Client.class));
        lenient().when(petRepository.findById(anyLong())).thenReturn(Optional.empty());
        lenient().when(petRepository.save(any(Pet.class))).thenReturn(null);
        lenient().when(probationPeriodRepository.findByClientId(anyLong())).thenReturn(PERIOD_1);
        lenient().when(probationPeriodRepository.save(any(ProbationPeriod.class))).thenReturn(PERIOD_1);
        lenient().doNothing().when(telegramBot).exec(any(SendMessage.class));
        lenient().when(dailyReportRepository.findAll()).thenReturn(DAILY_REPORTS);
        lenient().when(dailyReportRepository.findById(anyLong())).thenReturn(Optional.of(REPORT_1));
    }

    @Test
    void getAllClients() throws Exception {
        mockMvc.perform(get("/client/get")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(CLIENT_1.getId()))
               .andExpect(jsonPath("$[0].firstName").value(CLIENT_1.getFirstName()))
               .andExpect(jsonPath("$[0].lastName").value(CLIENT_1.getLastName()))
               .andExpect(jsonPath("$[0].userName").value(CLIENT_1.getUserName()))
               .andExpect(jsonPath("$[0].address").value(CLIENT_1.getAddress()))
               .andExpect(jsonPath("$[0].birthday").value(String.valueOf(CLIENT_1.getBirthday())))
               .andExpect(jsonPath("$[0].passport").value(CLIENT_1.getPassport()))
               .andExpect(jsonPath("$[0].chatId").value(CLIENT_1.getChatId()))
               .andExpect(jsonPath("$[1].id").value(CLIENT_2.getId()))
               .andExpect(jsonPath("$[1].firstName").value(CLIENT_2.getFirstName()))
               .andExpect(jsonPath("$[1].lastName").value(CLIENT_2.getLastName()))
               .andExpect(jsonPath("$[1].userName").value(CLIENT_2.getUserName()))
               .andExpect(jsonPath("$[1].address").value(CLIENT_2.getAddress()))
               .andExpect(jsonPath("$[1].birthday").value(String.valueOf(CLIENT_2.getBirthday())))
               .andExpect(jsonPath("$[1].passport").value(CLIENT_2.getPassport()))
               .andExpect(jsonPath("$[1].chatId").value(CLIENT_2.getChatId()))
               .andExpect(jsonPath("$[2].id").value(CLIENT_3.getId()))
               .andExpect(jsonPath("$[2].firstName").value(CLIENT_3.getFirstName()))
               .andExpect(jsonPath("$[2].lastName").value(CLIENT_3.getLastName()))
               .andExpect(jsonPath("$[2].userName").value(CLIENT_3.getUserName()))
               .andExpect(jsonPath("$[2].address").value(CLIENT_3.getAddress()))
               .andExpect(jsonPath("$[2].birthday").value(String.valueOf(CLIENT_3.getBirthday())))
               .andExpect(jsonPath("$[2].passport").value(CLIENT_3.getPassport()))
               .andExpect(jsonPath("$[2].chatId").value(CLIENT_3.getChatId()))
               .andExpect(jsonPath("$[3].id").value(CLIENT_4.getId()))
               .andExpect(jsonPath("$[3].firstName").value(CLIENT_4.getFirstName()))
               .andExpect(jsonPath("$[3].lastName").value(CLIENT_4.getLastName()))
               .andExpect(jsonPath("$[3].userName").value(CLIENT_4.getUserName()))
               .andExpect(jsonPath("$[3].address").value(CLIENT_4.getAddress()))
               .andExpect(jsonPath("$[3].birthday").value(String.valueOf(CLIENT_4.getBirthday())))
               .andExpect(jsonPath("$[3].passport").value(CLIENT_4.getPassport()))
               .andExpect(jsonPath("$[3].chatId").value(CLIENT_4.getChatId()))
               .andExpect(jsonPath("$[4].id").value(CLIENT_5.getId()))
               .andExpect(jsonPath("$[4].firstName").value(CLIENT_5.getFirstName()))
               .andExpect(jsonPath("$[4].lastName").value(CLIENT_5.getLastName()))
               .andExpect(jsonPath("$[4].userName").value(CLIENT_5.getUserName()))
               .andExpect(jsonPath("$[4].address").value(CLIENT_5.getAddress()))
               .andExpect(jsonPath("$[4].birthday").value(String.valueOf(CLIENT_5.getBirthday())))
               .andExpect(jsonPath("$[4].passport").value(CLIENT_5.getPassport()))
               .andExpect(jsonPath("$[4].chatId").value(CLIENT_5.getChatId()));
    }

    @Test
    void getClient() throws Exception {
        mockMvc.perform(get("/client/get/5")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(CLIENT_5.getId()))
               .andExpect(jsonPath("$.firstName").value(CLIENT_5.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(CLIENT_5.getLastName()))
               .andExpect(jsonPath("$.userName").value(CLIENT_5.getUserName()))
               .andExpect(jsonPath("$.address").value(CLIENT_5.getAddress()))
               .andExpect(jsonPath("$.birthday").value(String.valueOf(CLIENT_5.getBirthday())))
               .andExpect(jsonPath("$.passport").value(CLIENT_5.getPassport()))
               .andExpect(jsonPath("$.chatId").value(CLIENT_5.getChatId()));
    }

    @Test
    void writeClient() throws Exception {
        mockMvc.perform(post("/client/write?firstName=" + CLIENT_5.getFirstName() + "&lastName=" + CLIENT_5.getLastName() + "&userName=" + CLIENT_5.getUserName() + "&address=" + CLIENT_5.getAddress() + "&birthday=" + CLIENT_5.getBirthday() + "&passport=" + CLIENT_5.getPassport() + "&chatId=" + CLIENT_5.getChatId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(CLIENT_5.getId()))
               .andExpect(jsonPath("$.firstName").value(CLIENT_5.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(CLIENT_5.getLastName()))
               .andExpect(jsonPath("$.userName").value(CLIENT_5.getUserName()))
               .andExpect(jsonPath("$.address").value(CLIENT_5.getAddress()))
               .andExpect(jsonPath("$.birthday").value(String.valueOf(CLIENT_5.getBirthday())))
               .andExpect(jsonPath("$.passport").value(CLIENT_5.getPassport()))
               .andExpect(jsonPath("$.chatId").value(CLIENT_5.getChatId()));
    }

    @Test
    void editClient() throws Exception {
        mockMvc.perform(put("/client/edit?id=" + CLIENT_5.getId() + "&firstName=" + CLIENT_5.getFirstName() + "&lastName=" + CLIENT_5.getLastName() + "&userName=" + CLIENT_5.getUserName() + "&address=" + CLIENT_5.getAddress() + "&birthday=" + CLIENT_5.getBirthday() + "&passport=" + CLIENT_5.getPassport() + "&chatId=" + CLIENT_5.getChatId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(CLIENT_5.getId()))
               .andExpect(jsonPath("$.firstName").value(CLIENT_5.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(CLIENT_5.getLastName()))
               .andExpect(jsonPath("$.userName").value(CLIENT_5.getUserName()))
               .andExpect(jsonPath("$.address").value(CLIENT_5.getAddress()))
               .andExpect(jsonPath("$.birthday").value(String.valueOf(CLIENT_5.getBirthday())))
               .andExpect(jsonPath("$.passport").value(CLIENT_5.getPassport()))
               .andExpect(jsonPath("$.chatId").value(CLIENT_5.getChatId()));
    }

    @Test
    void deleteClient() throws Exception {
        mockMvc.perform(delete("/client/delete/5")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(CLIENT_5.getId()))
               .andExpect(jsonPath("$.firstName").value(CLIENT_5.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(CLIENT_5.getLastName()))
               .andExpect(jsonPath("$.userName").value(CLIENT_5.getUserName()))
               .andExpect(jsonPath("$.address").value(CLIENT_5.getAddress()))
               .andExpect(jsonPath("$.birthday").value(String.valueOf(CLIENT_5.getBirthday())))
               .andExpect(jsonPath("$.passport").value(CLIENT_5.getPassport()))
               .andExpect(jsonPath("$.chatId").value(CLIENT_5.getChatId()));
    }

    @Test
    void addDays() throws Exception {
        mockMvc.perform(put("/client/add?id=1&days=1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest());
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get("/client/reports")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(REPORT_1.getId()))
               .andExpect(jsonPath("$[0].date").value(String.valueOf(REPORT_1.getDate())))
               .andExpect(jsonPath("$[1].id").value(REPORT_2.getId()))
               .andExpect(jsonPath("$[1].date").value(String.valueOf(REPORT_2.getDate())))
               .andExpect(jsonPath("$[2].id").value(REPORT_3.getId()))
               .andExpect(jsonPath("$[2].date").value(String.valueOf(REPORT_3.getDate())));
    }

    @Test
    void getClientReports() throws Exception {
        mockMvc.perform(get("/client/reports/5?id=5")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isMap())
               .andExpect(jsonPath("$").hasJsonPath())
               .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void getClientReport() throws Exception {
        mockMvc.perform(get("/client/report/1?id=1")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isNotEmpty())
               .andExpect(jsonPath("$.id").value(REPORT_1.getId()))
               .andExpect(jsonPath("$.text").value(REPORT_1.getText()))
               .andExpect(jsonPath("$.date").value(String.valueOf(REPORT_1.getDate())))
               .andExpect(jsonPath("$.clientId").value(REPORT_1.getClientId()));
    }

    @Test
    void warning() throws Exception {
        mockMvc.perform(get("/client/warning/5?id=5")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }
}
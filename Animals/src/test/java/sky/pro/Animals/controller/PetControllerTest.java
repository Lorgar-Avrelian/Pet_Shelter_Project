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
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.entity.Pet;
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
class PetControllerTest {
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
    private PetController petController;

    @BeforeEach
    void init() {
        lenient().doNothing().when(infoService).checkInfo();
        lenient().when(petRepository.findAll()).thenReturn(PETS);
        lenient().when(petRepository.findById(anyLong())).thenReturn(Optional.of(CAT_5));
        lenient().when(petRepository.getById(anyLong())).thenReturn(CAT_5);
        lenient().when(petRepository.save(any(Pet.class))).thenReturn(CAT_5);
        lenient().doNothing().when(petRepository).delete(any(Pet.class));
        lenient().when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());
        lenient().when(clientRepository.save(any(Client.class))).thenReturn(null);
    }

    @Test
    void getAllPets() throws Exception {
        mockMvc.perform(get("/pet/get")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(CAT_1.getId()))
               .andExpect(jsonPath("$[0].name").value(CAT_1.getName()))
               .andExpect(jsonPath("$[0].birthday").value(String.valueOf(CAT_1.getBirthday())))
               .andExpect(jsonPath("$[0].petVariety").value(String.valueOf(CAT_1.getPetVariety())))
               .andExpect(jsonPath("$[1].id").value(CAT_2.getId()))
               .andExpect(jsonPath("$[1].name").value(CAT_2.getName()))
               .andExpect(jsonPath("$[1].birthday").value(String.valueOf(CAT_2.getBirthday())))
               .andExpect(jsonPath("$[1].petVariety").value(String.valueOf(CAT_2.getPetVariety())))
               .andExpect(jsonPath("$[2].id").value(CAT_3.getId()))
               .andExpect(jsonPath("$[2].name").value(CAT_3.getName()))
               .andExpect(jsonPath("$[2].birthday").value(String.valueOf(CAT_3.getBirthday())))
               .andExpect(jsonPath("$[2].petVariety").value(String.valueOf(CAT_3.getPetVariety())))
               .andExpect(jsonPath("$[3].id").value(CAT_4.getId()))
               .andExpect(jsonPath("$[3].name").value(CAT_4.getName()))
               .andExpect(jsonPath("$[3].birthday").value(String.valueOf(CAT_4.getBirthday())))
               .andExpect(jsonPath("$[3].petVariety").value(String.valueOf(CAT_4.getPetVariety())))
               .andExpect(jsonPath("$[4].id").value(CAT_5.getId()))
               .andExpect(jsonPath("$[4].name").value(CAT_5.getName()))
               .andExpect(jsonPath("$[4].birthday").value(String.valueOf(CAT_5.getBirthday())))
               .andExpect(jsonPath("$[4].petVariety").value(String.valueOf(CAT_5.getPetVariety())))
               .andExpect(jsonPath("$[5].id").value(DOG_1.getId()))
               .andExpect(jsonPath("$[5].name").value(DOG_1.getName()))
               .andExpect(jsonPath("$[5].birthday").value(String.valueOf(DOG_1.getBirthday())))
               .andExpect(jsonPath("$[5].petVariety").value(String.valueOf(DOG_1.getPetVariety())))
               .andExpect(jsonPath("$[6].id").value(DOG_2.getId()))
               .andExpect(jsonPath("$[6].name").value(DOG_2.getName()))
               .andExpect(jsonPath("$[6].birthday").value(String.valueOf(DOG_2.getBirthday())))
               .andExpect(jsonPath("$[6].petVariety").value(String.valueOf(DOG_2.getPetVariety())))
               .andExpect(jsonPath("$[7].id").value(DOG_3.getId()))
               .andExpect(jsonPath("$[7].name").value(DOG_3.getName()))
               .andExpect(jsonPath("$[7].birthday").value(String.valueOf(DOG_3.getBirthday())))
               .andExpect(jsonPath("$[7].petVariety").value(String.valueOf(DOG_3.getPetVariety())))
               .andExpect(jsonPath("$[8].id").value(DOG_4.getId()))
               .andExpect(jsonPath("$[8].name").value(DOG_4.getName()))
               .andExpect(jsonPath("$[8].birthday").value(String.valueOf(DOG_4.getBirthday())))
               .andExpect(jsonPath("$[8].petVariety").value(String.valueOf(DOG_4.getPetVariety())))
               .andExpect(jsonPath("$[9].id").value(DOG_5.getId()))
               .andExpect(jsonPath("$[9].name").value(DOG_5.getName()))
               .andExpect(jsonPath("$[9].birthday").value(String.valueOf(DOG_5.getBirthday())))
               .andExpect(jsonPath("$[9].petVariety").value(String.valueOf(DOG_5.getPetVariety())));
    }

    @Test
    void getPet() throws Exception {
        mockMvc.perform(get("/pet/get/5")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(CAT_5.getId()))
               .andExpect(jsonPath("$.name").value(CAT_5.getName()))
               .andExpect(jsonPath("$.birthday").value(String.valueOf(CAT_5.getBirthday())))
               .andExpect(jsonPath("$.petVariety").value(String.valueOf(CAT_5.getPetVariety())));
    }

    @Test
    void writePet() throws Exception {
        mockMvc.perform(post("/pet/write?name=" + CAT_5.getName() + "&birthday=" + CAT_5.getBirthday() + "&alive=true&petVariety=" + CAT_5.getPetVariety())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value(CAT_5.getName()))
               .andExpect(jsonPath("$.birthday").value(String.valueOf(CAT_5.getBirthday())))
               .andExpect(jsonPath("$.petVariety").value(String.valueOf(CAT_5.getPetVariety())));
    }

    @Test
    void editPet() throws Exception {
        mockMvc.perform(put("/pet/edit?id=" + CAT_5.getId() + "&name=" + CAT_5.getName() + "&birthday=" + CAT_5.getBirthday() + "&alive=true&petVariety=" + CAT_5.getPetVariety())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(CAT_5.getId()))
               .andExpect(jsonPath("$.name").value(CAT_5.getName()))
               .andExpect(jsonPath("$.birthday").value(String.valueOf(CAT_5.getBirthday())))
               .andExpect(jsonPath("$.petVariety").value(String.valueOf(CAT_5.getPetVariety())));
    }

    @Test
    void deletePet() throws Exception {
        mockMvc.perform(delete("/pet/delete/5?id=5")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(CAT_5.getId()))
               .andExpect(jsonPath("$.name").value(CAT_5.getName()))
               .andExpect(jsonPath("$.birthday").value(String.valueOf(CAT_5.getBirthday())))
               .andExpect(jsonPath("$.petVariety").value(String.valueOf(CAT_5.getPetVariety())));
    }
}
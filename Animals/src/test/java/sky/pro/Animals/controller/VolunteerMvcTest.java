package sky.pro.Animals.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import sky.pro.Animals.entity.Volunteer;
import sky.pro.Animals.repository.*;
import sky.pro.Animals.service.*;

import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(VolunteerController.class)
public class VolunteerMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private VolunteerController volunteerController;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private InfoRepository infoRepository;
    @MockBean
    private PetAvatarRepository petAvatarRepository;
    @MockBean
    private PetRepository petRepository;
    @MockBean
    private ProbationPeriodRepository probationPeriodRepository;
    @MockBean
    private VolunteerRepository volunteerRepository;

    @SpyBean
    private ClientServiceImpl clientService;
    @SpyBean
    private InfoServiceImpl infoService;
    @SpyBean
    private PetAvatarServiceImpl petAvatarService;
    @SpyBean
    private PetServiceImpl petService;
    @SpyBean
    private SchedulerServiceImpl schedulerService;
    @SpyBean
    private VolunteerServiceImpl volunteerService;

    @Test
    void crud() throws Exception {
        Volunteer volunteer = new Volunteer(1l, "FIO", "address", new Date(2000 - 11 - 11)
                , "234432", 1111l, "superMan");


        //create   post запрос
        when(volunteerRepository.save(any(Volunteer.class))).thenReturn(volunteer);

        mockMvc.perform(MockMvcRequestBuilders.post("/write")
                        .content(objectMapper.writeValueAsString(volunteer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(1l))
                .andExpect(jsonPath("$.address").value("address"));

    }
}

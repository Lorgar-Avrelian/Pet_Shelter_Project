package sky.pro.Animals.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.model.PetVariety;
import sky.pro.Animals.repository.*;
import sky.pro.Animals.service.*;

import java.sql.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class ClientWebMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private InfoRepository infoRepository;
    @MockBean
    private PetAvatarRepository petAvatarRepository;
    @MockBean
    private PetRepository petRepository;
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
    private VolunteerServiceImpl volunteerService;
    @InjectMocks
    private ClientController clientController;

    @Test
    void crud() throws Exception {
        Pet pet1 = new Pet(1l, "Name1", new Date(2023, 10, 10), true
                , PetVariety.cat, null);

        Pet pet2 = new Pet();

        List<Pet> pets = List.of(pet1);

        Client client1 = new Client(1l,"Fn","Ln","Un","addres"
                ,new Date(2000,11,11),"123321",111l,pets);
        //create   post запрос
        when(clientRepository.save(any(Client.class))).thenReturn(client1);

        mockMvc.perform(MockMvcRequestBuilders.post("/write")
                        .content(objectMapper.writeValueAsString(client1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isOk());
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("Faculty1"))
//                .andExpect(jsonPath("$.color").value("red"));

    }
}

package sky.pro.Animals.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.model.PetVariety;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PetControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
   private TestRestTemplate testRestTemplate;
    @Autowired
    private PetController petController;
    @Test
    public void contextLoads() throws Exception {             //контроллер создался и существует

        Assertions.assertThat(petController).isNotNull();
    }
//    @Test
//    void crud() {
//        Pet pet = new Pet();
//        Pet pet1 = new Pet(3l, "Name1", new Date(2023-10-10), true
//                , PetVariety.cat, null);
//        //        create post запрос
//        ResponseEntity<Pet> responseCreate = testRestTemplate.postForEntity("/pet/write?" +
//                "id=3&name=Name1&birthday=2023-10-10&alive=true&petVariety=cat&client=null", pet1, Pet.class);
//        Assertions.assertThat(responseCreate).isNotNull();
//        Assertions.assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        Pet respBody = responseCreate.getBody();// переменная с телом запроса
//        Assertions.assertThat(respBody).isNotNull();
//        Assertions.assertThat(respBody.getId()).isNotNull();
//        Assertions.assertThat(respBody.getName()).isNotNull();
//
//        Assertions.assertThat(respBody.getName()).isEqualTo("Name1");
//        Assertions.assertThat(respBody.getId()).isEqualTo(3l);
//    }
    @Test
    void getAllPets() {
    }

    @Test
    void getPet() {
    }

    @Test
    void writePet() {
    }

    @Test
    void editPet() {
    }

    @Test
    void deletePet() {
    }
}
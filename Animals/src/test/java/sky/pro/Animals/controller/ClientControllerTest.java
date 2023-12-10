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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ClientController clientController;

    @Test
    public void contextLoads() throws Exception {             //контроллер создался и существует

        Assertions.assertThat(clientController).isNotNull();
    }
    //похоже нужно сначала добавить животное прям в этом методе
//    @Test
//    public void crud() {
//        Pet pet1 = new Pet(1l, "Name1", new Date(2023-10-10), true
//                , PetVariety.cat, null);
//        Pet pet2 = new Pet();
//        List<Pet> pets = List.of(pet2);
//
//        Client client1 = new Client(1l,"Fn","Ln","Un","address1"
//                ,new Date(2000-11-11),"123321",111l,pets);
//        Client client = new Client();
////        http://localhost:8080/client/write?id=1&firstName=111
////        // &lastName=111&userName=111&address=111&birthday=1111-11-11&passport=111&chatId=111
////        create post запрос
//        ResponseEntity<Client> responseCreate = testRestTemplate.postForEntity("/client/write"+
//                "?id=1&firstName=Fn" +
//                "&lastName=Ln&userName=Un&address=address1" +
//                "&birthday=2000-11-11&passport=123321&chatId=111&firstPetId=1"
//                ,client, Client.class);
//
//        Assertions.assertThat(responseCreate).isNotNull();
//        Assertions.assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//
//    }
    @Test
    void getAllClients() {
    }

    @Test
    void getClient() {
    }

    @Test
    void writeClient() {
    }

    @Test
    void editClient() {
    }

    @Test
    void deleteClient() {
    }
}
/*@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchoolApplicationFacultyControllerSpringBootTestTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Test
    public void contextLoads() throws Exception {             //контроллер создался и существует

        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test//тест crud запросов
    public void crud() {
        Faculty faculty = new Faculty();
        faculty.setName("faculty");
        faculty.setColor("red");
//create   post запрос
        ResponseEntity<Faculty> responseCreate = restTemplate.postForEntity("/faculty", faculty, Faculty.class);//Student.class возвращаемый класс
        Assertions.assertThat(responseCreate).isNotNull();
        Assertions.assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.OK);//проверяем статус 200(ОК это 200)
        Faculty respBody = responseCreate.getBody();// переменная с телом запроса
        Assertions.assertThat(respBody).isNotNull();
        Assertions.assertThat(respBody.getId()).isNotNull();
        Assertions.assertThat(respBody.getName()).isNotNull();

        Assertions.assertThat(respBody.getName()).isEqualTo("faculty");
        Assertions.assertThat(respBody.getColor()).isEqualTo("red");
//read  get запрос
        ResponseEntity<Faculty> responseGet = restTemplate.getForEntity("/faculty/" + respBody.getId(), Faculty.class);
        Assertions.assertThat(responseGet).isNotNull();
        Assertions.assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty respBody1 = responseGet.getBody();// переменная с телом запроса
        Assertions.assertThat(respBody1).isNotNull();
        Assertions.assertThat(respBody1.getId()).isNotNull();
        Assertions.assertThat(respBody1.getName()).isEqualTo("faculty");
        Assertions.assertThat(respBody1.getColor()).isEqualTo("red");
//  update  put запрос


        Faculty facultyCheng = new Faculty();

        facultyCheng.setName("facultyCheng");
        facultyCheng.setColor("Green");

        restTemplate.put("/faculty/" + respBody.getId(), facultyCheng);

        ResponseEntity<Faculty> responseGet1 = restTemplate.getForEntity("/faculty/" + respBody.getId(), Faculty.class);
        Assertions.assertThat(responseGet1).isNotNull();
        Assertions.assertThat(responseGet1.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty respBody2 = responseGet1.getBody();// переменная с телом запроса
        Assertions.assertThat(respBody2).isNotNull();
        Assertions.assertThat(respBody2.getId()).isNotNull();
        Assertions.assertThat(respBody2.getName()).isEqualTo("facultyCheng");
        Assertions.assertThat(respBody2.getColor()).isEqualTo("Green");
//  delete  запрос
        restTemplate.delete("/faculty?id=" + respBody.getId());


        ResponseEntity<Faculty> responseGetForDelete = restTemplate.getForEntity("/faculty/" + respBody.getId(), Faculty.class);

        Assertions.assertThat(responseGetForDelete).isNotNull();
        Assertions.assertThat(responseGetForDelete.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);


    }

    @Test
    public void findByNameContainsIgnoreCaseOrColorContainsIgnoreCase() {
        Faculty faculty = new Faculty();
        faculty.setName("faculty");
        faculty.setColor("Green");

        ResponseEntity<Faculty> responseCreate = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        Assertions.assertThat(responseCreate).isNotNull();
        Assertions.assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseCreate.getBody().getName()).isEqualTo("faculty");


        ResponseEntity<Collection> responseGet = restTemplate.getForEntity("/faculty/name-or-color?param=" + "fac", Collection.class);
        Assertions.assertThat(responseGet).isNotNull();
        Assertions.assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseGet).isNotNull();


    }


    @Test
    public void studentByIdFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("faculty");
        faculty.setColor("red");

        ResponseEntity<Faculty> responseCreateFaculty = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        Assertions.assertThat(responseCreateFaculty).isNotNull();
        Assertions.assertThat(responseCreateFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty respBodyFa = responseCreateFaculty.getBody();
        Long facultyId = responseCreateFaculty.getBody().getId();

        Student student = new Student();
        student.setName("Bob");
        student.setAge(20);
        student.setFaculty(respBodyFa);

        ResponseEntity<Student> responseCreateStudent = restTemplate.postForEntity("/student", student, Student.class);
        Assertions.assertThat(responseCreateStudent).isNotNull();
        Assertions.assertThat(responseCreateStudent.getStatusCode()).isEqualTo(HttpStatus.OK);


        ResponseEntity<Collection> responseGet = restTemplate.getForEntity("/faculty/student-byIdFaculty?id="
                + facultyId, Collection.class);
        Assertions.assertThat(responseGet).isNotNull();
        Assertions.assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseGet.getBody()).isNotNull();

    }


}
*/
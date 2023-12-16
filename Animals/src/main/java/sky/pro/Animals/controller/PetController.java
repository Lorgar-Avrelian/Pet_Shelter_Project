package sky.pro.Animals.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.model.PetVariety;
import sky.pro.Animals.service.*;

import java.sql.Date;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Controller for CRUD operations with pets data
 * <p>
 * <hr>
 * <p>
 * Контроллер для CRUD операций с данными питомцев
 */
@RestController
@RequestMapping(path = "/pet")
public class PetController {
    private final PetServiceImpl petService;
    private final InfoServiceImpl infoService;
    private final ClientServiceImpl clientService;

    public PetController(PetServiceImpl petService, InfoServiceImpl infoService, ClientServiceImpl clientService) {
        this.petService = petService;
        this.infoService = infoService;
        this.clientService = clientService;
    }

    /**
     * API for getting collection with all pets in pet shelter. <br>
     * Used service method {@link PetService#getAll()}. <br>
     * <hr>
     * API для получения коллекции со всеми питомцами приюта. <br>
     * Использован метод сервиса {@link PetService#getAll()}. <br>
     * <hr>
     *
     * @return Collection with all pets / Коллекцию со всеми питомцами
     * @see PetService#getAll()
     */
    @GetMapping(path = "/get")
    public ResponseEntity<Collection<Pet>> getAllPets() {
        infoService.checkInfo();
        Collection<Pet> pets = petService.getAll();
        if (pets == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(pets);
        }
    }

    /**
     * API for getting info about pet with this id. <br>
     * Used service method {@link PetService#getById(Long)}. <br>
     * <hr>
     * API для получения информации о питомце с данным id. <br>
     * Использован метод сервиса {@link PetService#getById(Long)}. <br>
     * <hr>
     *
     * @param id
     * @return Pet if exist / Питомца, если таковой есть
     * @see PetService#getById(Long)
     */
    @GetMapping(path = "/get/{id}")
    public ResponseEntity<Pet> getPet(@PathVariable Long id) {
        infoService.checkInfo();
        Pet pet = petService.getById(id);
        if (pet == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(pet);
        }
    }

    /**
     * API for saving pet with this params. <br>
     * Used service methods {@link ClientService#save(Client)} {@link PetService#save(Pet)}. <br>
     * <hr>
     * API для сохранения питомца с данными параметрами. <br>
     * Использованы методы сервисов {@link ClientService#save(Client)} {@link PetService#save(Pet)}. <br>
     * <hr>
     *
     * @param name
     * @param birthday
     * @param alive
     * @param petVariety
     * @param clientId
     * @return Saved pet / Сохранённого питомца
     * @see ClientService#save(Client)
     * @see PetService#save(Pet)
     */
    @PostMapping(path = "/write")
    public ResponseEntity<Pet> writePet(@RequestParam String name,
                                        @RequestParam Date birthday,
                                        @RequestParam boolean alive,
                                        @RequestParam PetVariety petVariety,
                                        @RequestParam(required = false) Long clientId) {
        infoService.checkInfo();
        Pet savedPet = petService.save(0L, name, birthday, alive, petVariety, null);
        Client client;
        if (clientId == null) {
            client = null;
        } else if (clientService.getById(clientId) == null) {
            client = null;
        } else {
            client = clientService.getById(clientId);
        }
        Collection<Pet> clientPets;
        if (client != null) {
            clientPets = clientService.getById(clientId).getPets();
            clientPets.add(savedPet);
            client.setPets(clientPets);
            clientService.save(client);
            savedPet.setClient(clientService.getById(clientId));
            petService.save(savedPet);
        }
        if (savedPet == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(savedPet);
        }
    }

    /**
     * API for editing pet with this params. <br>
     * Used service methods {@link ClientService#save(Client)} {@link PetService#save(Pet)}. <br>
     * <hr>
     * API для редактирования питомца с данными параметрами. <br>
     * Использованы методы сервисов {@link ClientService#save(Client)} {@link PetService#save(Pet)}. <br>
     * <hr>
     *
     * @param id
     * @param name
     * @param birthday
     * @param alive
     * @param petVariety
     * @param clientId
     * @return Edited pet / Отредактированного питомца
     * @see ClientService#save(Client)
     * @see PetService#save(Pet)
     */
    @PutMapping(path = "/edit")
    public ResponseEntity<Pet> editPet(@RequestParam Long id,
                                       @RequestParam String name,
                                       @RequestParam Date birthday,
                                       @RequestParam boolean alive,
                                       @RequestParam PetVariety petVariety,
                                       @RequestParam(required = false) Long clientId) {
        infoService.checkInfo();
        Client client;
        if (clientId == null) {
            client = null;
        } else if (clientService.getById(clientId) == null) {
            client = null;
        } else {
            client = clientService.getById(clientId);
        }
        Pet pet = new Pet(id, name, birthday, alive, petVariety, client);
        Collection<Pet> clientPets;
        if (client != null) {
            clientPets = clientService.getById(clientId).getPets();
            clientPets.add(pet);
            client.setPets(clientPets);
            clientService.save(client);
        }
        Pet editedPet = petService.save(pet);
        if (editedPet == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(editedPet);
        }
    }

    /**
     * API for deleting pet with this id. <br>
     * Used service methods {@link ClientService#save(Client)} {@link PetService#delete(Long)}. <br>
     * <hr>
     * API для удаления питомца с данным id. <br>
     * Использованы методы сервисов {@link ClientService#save(Client)} {@link PetService#delete(Long)}. <br>
     * <hr>
     *
     * @param id
     * @return Deleted pet / Удалённого питомца
     * @see ClientService#save(Client)
     * @see PetService#delete(Long)
     */
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Pet> deletePet(@PathVariable Long id) {
        infoService.checkInfo();
        Client client = petService.getById(id).getClient();
        if (client != null) {
            Collection<Pet> clientPets = client.getPets().stream()
                                               .filter(pet -> !pet.equals(petService.getById(id)))
                                               .collect(Collectors.toCollection(LinkedList::new));
            client.setPets(clientPets);
            clientService.save(client);
        }
        Pet deletedPet = petService.delete(id);
        if (deletedPet == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(deletedPet);
        }
    }
}

package sky.pro.Animals.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.service.InfoServiceImpl;
import sky.pro.Animals.service.PetServiceImpl;

import java.util.Collection;

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

    public PetController(PetServiceImpl petService, InfoServiceImpl infoService) {
        this.petService = petService;
        this.infoService = infoService;
    }

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

    @PostMapping(path = "/write")
    public ResponseEntity<Pet> writePet(@RequestParam Pet pet) {
        infoService.checkInfo();
        Pet savedPet = petService.save(pet);
        if (savedPet == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(savedPet);
        }
    }

    @PutMapping(path = "/edit/{id}")
    public ResponseEntity<Pet> editPet(@PathVariable Long id, @RequestParam Pet pet) {
        infoService.checkInfo();
        Pet editedPet = petService.save(pet);
        if (editedPet == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(editedPet);
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Pet> deletePet(@PathVariable Long id) {
        infoService.checkInfo();
        Pet deletedPet = petService.delete(id);
        if (deletedPet == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(deletedPet);
        }
    }
}

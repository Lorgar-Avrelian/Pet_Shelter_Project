package sky.pro.Animals.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.service.PetServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping(path = "/pet")
public class PetController {
    private final PetServiceImpl petService;

    public PetController(PetServiceImpl petService) {
        this.petService = petService;
    }

    @GetMapping(path = "/get")
    public ResponseEntity<Collection<Pet>> getAllPets() {
        Collection<Pet> pets = petService.getAll();
        if (pets == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(pets);
        }
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<Pet> getPet(@PathVariable Long id) {
        Pet pet = petService.getById(id);
        if (pet == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(pet);
        }
    }

    @PostMapping(path = "/write")
    public ResponseEntity<Pet> writePet(@RequestParam Pet pet) {
        Pet savedPet = petService.save(pet);
        if (savedPet == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(savedPet);
        }
    }

    @PutMapping(path = "/edit/{id}")
    public ResponseEntity<Pet> editPet(@PathVariable Long id, @RequestParam Pet pet) {
        Pet editedPet = petService.save(pet);
        if (editedPet == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(editedPet);
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Pet> deletePet(@PathVariable Long id) {
        Pet deletedPet = petService.delete(id);
        if (deletedPet == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(deletedPet);
        }
    }
}

package sky.pro.Animals.service;

import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.model.PetVariety;

import java.util.Collection;
import java.util.List;

public interface PetService {
    Collection<Pet> getAll();

    Pet getById(Long id);

    Pet save(Pet pet);

    Pet delete(Long id);

    List<Pet> getPetListByVariety(PetVariety petVariety);
}

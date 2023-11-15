package sky.pro.Animals.service;

import org.springframework.stereotype.Service;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.repository.PetRepository;

import java.util.Collection;

@Service
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Collection<Pet> getAll() {
        return petRepository.findAll();
    }

    @Override
    public Pet getById(Long id) {
        return petRepository.findById(id).get();
    }

    @Override
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    @Override
    public Pet delete(Long id) {
        Pet pet = petRepository.getById(id);
        petRepository.delete(pet);
        return pet;
    }
}

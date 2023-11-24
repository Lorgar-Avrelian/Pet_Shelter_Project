package sky.pro.Animals.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.repository.PetRepository;

import java.util.Collection;

/**
 * Service for working with pets data
 * <p>
 * <hr>
 * <p>
 * Сервис для работы с данными питомцев
 */
@Service
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * Method for getting collection with all pets. <br>
     * Used repository method {@link JpaRepository#findAll()}
     * <hr>
     * Метод для получения коллекции, содержащей всех питомцев. <br>
     * Используется метод репозитория (@link JpaRepository#findAll())
     * <hr>
     *
     * @return Collection with all pets / Коллекцию со всеми питомцами
     * @see JpaRepository#findAll()
     */
    @Override
    public Collection<Pet> getAll() {
        return petRepository.findAll();
    }

    /**
     * Method for getting pet with this id. <br>
     * Used repository method {@link JpaRepository#findById(Object)}
     * <hr>
     * Метод для получения питомца, имеющего данный id. <br>
     * Используется метод репозитория (@link JpaRepository#findAll())
     * <hr>
     *
     * @param id of pet / id питомца
     * @return Pet with this id / Питомца с данным id
     * @see JpaRepository#findById(Object)
     */
    @Override
    public Pet getById(Long id) {
        return petRepository.findById(id).get();
    }


    /**
     * Method for saving pet in DB. <br>
     * Used repository method {@link JpaRepository#save(Object)}
     * <hr>
     * Метод для сохранения питомца в БД. <br>
     * Используется метод репозитория (@link JpaRepository#findAll())
     * <hr>
     *
     * @param pet / питомец
     * @return saved pet / сохраненного питомца
     * @see JpaRepository#save(Object)
     */
    @Override
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    /**
     * Method for deleting pet with this id from DB. <br>
     * Used repository method {@link JpaRepository#delete(Object)}
     * <hr>
     * Метод для удаления питомца с данным id из БД. <br>
     * Используется метод репозитория (@link JpaRepository#findAll())
     * <hr>
     *
     * @param id of pet / id питомца
     * @return deleted pet / удаленного питомца
     * @see JpaRepository#delete(Object)
     */
    @Override
    public Pet delete(Long id) {
        Pet pet = petRepository.getById(id);
        petRepository.delete(pet);
        return pet;
    }
}

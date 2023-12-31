package sky.pro.Animals.service;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.model.PetVariety;
import sky.pro.Animals.repository.PetRepository;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

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
     * Используется метод репозитория {@link JpaRepository#findAll()}
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
     * Используется метод репозитория {@link JpaRepository#findById(Object)}
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
     * Method for saving pet in DB when edit. <br>
     * Used repository method {@link JpaRepository#save(Object)}
     * <hr>
     * Метод для сохранения питомца в БД при редактировании. <br>
     * Используется метод репозитория {@link JpaRepository#save(Object)}
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
     * Method for saving new pet in DB. <br>
     * Used repository method {@link JpaRepository#save(Object)}
     * <hr>
     * Метод для сохранения нового питомца в БД. <br>
     * Используется метод репозитория {@link JpaRepository#save(Object)}
     * <hr>
     *
     * @param name
     * @param birthday
     * @param alive
     * @param petVariety
     * @return saved pet / сохраненного питомца
     * @see JpaRepository#save(Object)
     */
    @Override
    public Pet save(Long id,
                    String name,
                    Date birthday,
                    boolean alive,
                    PetVariety petVariety,
                    Client client) {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(name);
        pet.setBirthday(birthday);
        pet.setAlive(alive);
        pet.setPetVariety(petVariety);
        pet.setClient(client);
        return petRepository.save(pet);
    }

    /**
     * Method for deleting pet with this id from DB. <br>
     * Used repository method {@link JpaRepository#delete(Object)} <br>
     * Also used repository method {@link JpaRepository#findById(Object)}
     * <hr>
     * Метод для удаления питомца с данным id из БД. <br>
     * Используется метод репозитория {@link JpaRepository#delete(Object)} <br>
     * Также используется метод репозитория {@link JpaRepository#findById(Object)}
     * <hr>
     *
     * @param id of pet / id питомца
     * @return deleted pet / удаленного питомца
     * @see JpaRepository#delete(Object)
     * @see JpaRepository#findById(Object)
     */
    @Override
    public Pet delete(Long id) {
        Pet pet = petRepository.getById(id);
        petRepository.delete(pet);
        return pet;
    }

    /**
     * Method for forming a string with a list of animals of a certain species <br>
     * <hr>
     * Метод для формирования строки со списком животных определенного вида <br>
     * <hr>
     *
     * @param petVariety
     * @return string with a list of animals / строку со списком животных
     */
    @Override
    public List<Pet> getPetListByVariety(PetVariety petVariety) {
        return petRepository.findAllByPetVariety(petVariety);
    }
}

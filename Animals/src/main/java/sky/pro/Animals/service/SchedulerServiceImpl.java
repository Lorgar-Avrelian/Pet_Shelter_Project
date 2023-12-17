package sky.pro.Animals.service;

import org.springframework.stereotype.Service;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.entity.ProbationPeriod;
import sky.pro.Animals.repository.ProbationPeriodRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for working with notifications
 * <p>
 * <hr>
 * <p>
 * Сервис для работы с оповещениями
 */
@Service
public class SchedulerServiceImpl implements SchedulerService {
    private final ProbationPeriodRepository probation;
    private final PetServiceImpl petService;

    public SchedulerServiceImpl(ProbationPeriodRepository probation, PetServiceImpl petService) {
        this.probation = probation;
        this.petService = petService;
    }

    /**
     * Method for getting list with all registered probation periods. <br>
     * Used repository method {@link ProbationPeriodRepository#findAll()}. <br>
     * <hr>
     * Метод для получения списка со всеми зарегистрированными испытательными сроками. <br>
     * использован метод репозитория {@link ProbationPeriodRepository#findAll()}. <br>
     * <hr>
     *
     * @return List with all probation periods / Список со всеми испытательными сроками
     * @see ProbationPeriodRepository#findAll()
     */
    @Override
    public List<ProbationPeriod> getProbation() {
        return probation.findAll();
    }

    /**
     * Method for updating probation periods in list of probation periods. <br>
     * Used service method {@link PetService#getAll()} and repository method {@link ProbationPeriodRepository#save(Object)}. <br>
     * <hr>
     * Метод для обновления испытательных сроков в списке испытательных сроков. <br>
     * Использованы метод сервиса {@link PetService#getAll()} и метод репозитория {@link ProbationPeriodRepository#save(Object)}. <br>
     * <hr>
     *
     * @see PetService#getAll()
     * @see ProbationPeriodRepository#save(Object)
     */
    @Override
    public void updateProbation() {
        List<ProbationPeriod> probationPeriodList = probation.findAll();
        List<Long> petsOnProbation = probationPeriodList.stream()
                                                        .map(ProbationPeriod::getPetId)
                                                        .toList();
        List<Pet> pets = petService.getAll().stream()
                                   .filter(pet -> pet.getClient() != null)
                                   .toList();
        for (Pet pet : pets) {
            if (!petsOnProbation.contains(pet.getId())) {
                ProbationPeriod probationPeriod = new ProbationPeriod();
                probationPeriod.setPetId(pet.getId());
                probationPeriod.setClientId(pet.getClient().getId());
                LocalDate localDate = LocalDate.now();
                Date lastDate = Date.valueOf(localDate.plusMonths(1));
                probationPeriod.setLastDate(lastDate);
                probation.save(probationPeriod);
            }
        }
    }

    /**
     * Method for deleting information about probation period from probation periods list. <br>
     * Used repository method {@link ProbationPeriodRepository#delete(Object)}. <br>
     * <hr>
     * Метод для удаления информации об испытательном сроке из перечны испытательных сроков. <br>
     * Использован метод репозитория {@link ProbationPeriodRepository#delete(Object)}. <br>
     * <hr>
     *
     * @param id
     * @see ProbationPeriodRepository#delete(Object)
     */
    @Override
    public void deleteProbation(Long id) {
        ProbationPeriod deletedProbation = probation.findById(id).get();
        probation.delete(deletedProbation);
    }
}

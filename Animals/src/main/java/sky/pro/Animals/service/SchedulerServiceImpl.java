package sky.pro.Animals.service;

import org.springframework.stereotype.Service;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.entity.ProbationPeriod;
import sky.pro.Animals.repository.ProbationPeriodRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class SchedulerServiceImpl implements SchedulerService {
    private final ProbationPeriodRepository probation;
    private final PetServiceImpl petService;

    public SchedulerServiceImpl(ProbationPeriodRepository probation, PetServiceImpl petService) {
        this.probation = probation;
        this.petService = petService;
    }

    @Override
    public List<ProbationPeriod> getProbation() {
        return probation.findAll();
    }

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
                LocalDate localDate = LocalDate.ofEpochDay(System.currentTimeMillis());
                localDate.plusMonths(1);
                probationPeriod.setLastDate(Date.valueOf(localDate));
                probation.save(probationPeriod);
            }
        }
    }

    @Override
    public void deleteProbation(Long id) {
        ProbationPeriod deletedProbation = probation.findById(id).get();
        probation.delete(deletedProbation);
    }
}

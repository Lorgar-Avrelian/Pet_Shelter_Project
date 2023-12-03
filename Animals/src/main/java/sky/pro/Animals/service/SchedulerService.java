package sky.pro.Animals.service;

import sky.pro.Animals.entity.ProbationPeriod;

import java.util.List;

public interface SchedulerService {
    List<ProbationPeriod> getProbation();

    void updateProbation();

    void deleteProbation(Long id);
}

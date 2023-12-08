package sky.pro.Animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sky.pro.Animals.entity.ProbationPeriod;

@Repository
public interface ProbationPeriodRepository extends JpaRepository<ProbationPeriod, Long> {
    ProbationPeriod findByClientId(Long id);
}

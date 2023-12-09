package sky.pro.Animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sky.pro.Animals.entity.DailyReport;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
}

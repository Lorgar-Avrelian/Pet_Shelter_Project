package sky.pro.Animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.Animals.entity.Info;

public interface InfoRepository extends JpaRepository<Info, Long> {
}

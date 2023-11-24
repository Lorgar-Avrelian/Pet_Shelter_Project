package sky.pro.Animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.Animals.entity.Volunteer;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
}

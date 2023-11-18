package sky.pro.Animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.Animals.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
}

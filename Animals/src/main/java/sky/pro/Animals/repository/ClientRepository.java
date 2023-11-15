package sky.pro.Animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.Animals.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}

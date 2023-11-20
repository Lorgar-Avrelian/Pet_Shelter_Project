package sky.pro.Animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sky.pro.Animals.entity.Client;
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}

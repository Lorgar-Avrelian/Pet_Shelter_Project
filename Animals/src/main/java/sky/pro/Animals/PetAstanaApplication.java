package sky.pro.Animals;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@OpenAPIDefinition
@EnableScheduling
@EnableCaching(proxyTargetClass = true)
public class PetAstanaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetAstanaApplication.class, args);
	}
}

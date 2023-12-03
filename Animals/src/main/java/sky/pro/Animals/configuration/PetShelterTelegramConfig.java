package sky.pro.Animals.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@PropertySource("application.properties")
public class PetShelterTelegramConfig {
    @Value("${telegram.bot.name}")
    String nameBot;
    @Value("${telegram.bot.token}")
    String token;

    public PetShelterTelegramConfig() {
    }

    public PetShelterTelegramConfig(String nameBot, String token) {
        this.nameBot = nameBot;
        this.token = token;
    }

    public String getNameBot() {
        return nameBot;
    }

    public String getToken() {
        return token;
    }
}
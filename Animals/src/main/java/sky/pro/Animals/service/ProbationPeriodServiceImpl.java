package sky.pro.Animals.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.entity.ProbationPeriod;
import sky.pro.Animals.listener.PetShelterTelegramBot;
import sky.pro.Animals.repository.ProbationPeriodRepository;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class ProbationPeriodServiceImpl implements ProbationPeriodService {
    private final ProbationPeriodRepository probationPeriodRepository;
    private final PetShelterTelegramBot telegramBot;

    public ProbationPeriodServiceImpl(ProbationPeriodRepository probationPeriodRepository, PetShelterTelegramBot telegramBot) {
        this.probationPeriodRepository = probationPeriodRepository;
        this.telegramBot = telegramBot;
    }

    public Client changeLastDay(Client client, int days) {
        ProbationPeriod probationPeriod = probationPeriodRepository.findByClientId(client.getId());
        if (probationPeriod == null) {
            return null;
        }
        LocalDate lastDate = probationPeriod.getLastDate().toLocalDate().plusDays(days);
        probationPeriod.setLastDate(Date.valueOf(lastDate));
        probationPeriodRepository.save(probationPeriod);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(client.getChatId());
        sendMessage.setText("Дорогой усыновитель, вынужден сообщить, что Вам добавлено " + days + " дней испытательного срока!");
        telegramBot.exec(sendMessage);
        return client;
    }
}

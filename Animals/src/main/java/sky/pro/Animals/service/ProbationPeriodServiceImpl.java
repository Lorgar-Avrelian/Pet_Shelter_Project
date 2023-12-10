package sky.pro.Animals.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.entity.ProbationPeriod;
import sky.pro.Animals.listener.PetShelterTelegramBot;
import sky.pro.Animals.repository.ProbationPeriodRepository;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Service for working with probation periods data
 * <p>
 * <hr>
 * <p>
 * Сервис для работы с данными об испытательных сроках
 */
@Service
public class ProbationPeriodServiceImpl implements ProbationPeriodService {
    private final ProbationPeriodRepository probationPeriodRepository;
    private final PetShelterTelegramBot telegramBot;

    public ProbationPeriodServiceImpl(ProbationPeriodRepository probationPeriodRepository, PetShelterTelegramBot telegramBot) {
        this.probationPeriodRepository = probationPeriodRepository;
        this.telegramBot = telegramBot;
    }

    /**
     * Method for changing date of probation period end. <br>
     * Used repository methods {@link ProbationPeriodRepository#findByClientId(Long)} and {@link ProbationPeriodRepository#save(Object)}. <br>
     * <hr>
     * Метод для изменения даты окончания испытательного срока. <br>
     * Использованы методы репозитория {@link ProbationPeriodRepository#findByClientId(Long)} и {@link ProbationPeriodRepository#save(Object)}. <br>
     * <hr>
     *
     * @param client
     * @param days
     * @return Client on probation period / Клиента на испытательном сроке
     * @see ProbationPeriodRepository#findByClientId(Long)
     * @see ProbationPeriodRepository#save(Object)
     */
    @Override
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

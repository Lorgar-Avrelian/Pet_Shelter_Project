package sky.pro.Animals.listener;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sky.pro.Animals.configuration.PetShelterTelegramConfig;
import sky.pro.Animals.entity.*;
import sky.pro.Animals.service.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static sky.pro.Animals.model.PetVariety.cat;
import static sky.pro.Animals.model.PetVariety.dog;

/**
 * Main class of telegram bot
 * <p>
 * <hr>
 * <p>
 * Основной класс телеграм бота
 */
@Component
@Log4j
@EnableScheduling
public class PetShelterTelegramBot extends TelegramLongPollingBot {
    @Value("${daily.report.dir.path}")
    String reportPath;
    private final PetServiceImpl petService;
    private final PetAvatarServiceImpl petAvatarService;
    private final ClientServiceImpl clientService;
    private final InfoServiceImpl infoService;
    private final PetShelterTelegramConfig botConfig;
    private final VolunteerServiceImpl volunteerService;
    private final SchedulerServiceImpl schedulerService;
    private final DailyReportServiceImpl dailyReportService;
    private final Set<Long> registrationStatus = new HashSet<>();
    private final Set<Long> dailyReportStatus = new HashSet<>();

    public PetShelterTelegramBot(PetServiceImpl petService,
                                 PetAvatarServiceImpl petAvatarService,
                                 ClientServiceImpl clientService,
                                 InfoServiceImpl infoService,
                                 PetShelterTelegramConfig botConfig,
                                 VolunteerServiceImpl volunteerService,
                                 SchedulerServiceImpl schedulerService,
                                 DailyReportServiceImpl dailyReportService) {
        this.petService = petService;
        this.petAvatarService = petAvatarService;
        this.clientService = clientService;
        this.infoService = infoService;
        this.botConfig = botConfig;
        this.volunteerService = volunteerService;
        this.schedulerService = schedulerService;
        this.dailyReportService = dailyReportService;
        try {
            List<BotCommand> botCommands = new ArrayList<>(List.of(
                    new BotCommand("/start", "Информация о приюте"),
                    new BotCommand("/how_to_take", "Как взять животное из приюта"),
                    new BotCommand("/work_schedule", "Расписание работы приюта"),
                    new BotCommand("/address", "Адрес приюта"),
                    new BotCommand("/driving_directions", "Схема проезда"),
                    new BotCommand("/car_pass", "Контактные данные охраны для оформления пропуска на машину"),
                    new BotCommand("/safety_recommendations", "Общие рекомендации о технике безопасности на территории приюта"),
                    new BotCommand("/rules", "Правила знакомства с животным до того, как забрать его из приюта"),
                    new BotCommand("/documents_required", "Список документов, необходимых для того, чтобы взять животное из приюта"),
                    new BotCommand("/animal_transportation", "Список рекомендаций по транспортировке животного"),
                    new BotCommand("/home_improvement_puppy", "Список рекомендаций по обустройству дома для щенка"),
                    new BotCommand("/home_improvement_kitten", "Список рекомендаций по обустройству дома для котенка"),
                    new BotCommand("/adult_animal_recommendations", "Список рекомендаций по обустройству дома для взрослого животного с ограниченными возможностями"),
                    new BotCommand("/dog_communication", "Советы кинолога по первичному общению с собакой"),
                    new BotCommand("/dog_handlers", "Рекомендации по проверенным кинологам для дальнейшего обращения с собакой"),
                    new BotCommand("/may_refuse", "Список причин, почему могут отказать и не дать забрать собаку из приюта"),
                    new BotCommand("/daily_report", "Форма ежедневного отчета")
                                                                  ));
            this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getNameBot();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getUserName();
            if (clientService.getByChatId(chatId) == null) {
                Client client = new Client();
                client.setUserName(userName);
                client.setChatId(chatId);
                clientService.save(client);
            }
            switch (message) {
                case "/start" -> {
                    sendMessage(chatId, infoService.getInfoTextById(1L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/how_to_take" -> {
                    sendMessage(chatId, infoService.getInfoTextById(2L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/work_schedule" -> {
                    sendMessage(chatId, infoService.getInfoTextById(3L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/address" -> {
                    sendMessage(chatId, infoService.getInfoTextById(4L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/driving_directions" -> {
                    sendMessage(chatId, infoService.getInfoTextById(5L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/car_pass" -> {
                    sendMessage(chatId, infoService.getInfoTextById(6L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/safety_recommendations" -> {
                    sendMessage(chatId, infoService.getInfoTextById(7L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/rules" -> {
                    sendMessage(chatId, infoService.getInfoTextById(8L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/documents_required" -> {
                    sendMessage(chatId, infoService.getInfoTextById(9L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/animal_transportation" -> {
                    sendMessage(chatId, infoService.getInfoTextById(10L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/home_improvement_puppy" -> {
                    sendMessage(chatId, infoService.getInfoTextById(11L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/home_improvement_kitten" -> {
                    sendMessage(chatId, infoService.getInfoTextById(12L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/adult_animal_recommendations" -> {
                    sendMessage(chatId, infoService.getInfoTextById(13L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/dog_communication" -> {
                    sendMessage(chatId, infoService.getInfoTextById(14L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/dog_handlers" -> {
                    sendMessage(chatId, infoService.getInfoTextById(15L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/may_refuse" -> {
                    sendMessage(chatId, infoService.getInfoTextById(16L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "/daily_report" -> {
                    sendMessage(chatId, infoService.getInfoTextById(17L) + "\n\nДля получения дополнительной информации, пожалуйста, воспользуйтесь Menu бота.");
                }
                case "Кошачий приют" -> {
                    catsList(chatId);
                }
                case "Собачий приют" -> {
                    dogsList(chatId);
                }
                case "Связаться с волонтёром" -> {
                    callVolunteer(chatId, userName);
                }
                case "Пройти регистрацию" -> {
                    registration(chatId, null);
                }
                case "Посмотреть свои данные" -> {
                    showData(chatId);
                }
                default -> {
                    createAnswer(update);
                }
            }
        } else if (update.hasCallbackQuery()) {
            String message = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (!message.equals("/close-registration") && !message.equals("Отправить отчёт")) {
                getPet(message, chatId);
            } else if (message.equals("/close-registration")) {
                registrationStop(chatId);
            } else {
                takeReport(chatId);
            }
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            Long chatId = update.getMessage().getChatId();
            String message = update.getMessage().getCaption();
            List<PhotoSize> photos = update.getMessage().getPhoto();
            GetFile getFile = new GetFile(photos.get(photos.size() - 1).getFileId());
            try {
                org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
                downloadFile(file, new java.io.File(reportPath + (photos.size() - 1) + ".png"));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
            try {
                byte[] photoInBytes = Files.readAllBytes(Path.of(reportPath + (photos.size() - 1) + ".png"));
                if (dailyReportStatus.contains(chatId)) {
                    dailyReport(chatId, message, photoInBytes);
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(0, "Кошачий приют");
        keyboardRow.add(1, "Собачий приют");
        keyboardRows.add(keyboardRow);
        keyboardRow = new KeyboardRow();
        keyboardRow.add("Связаться с волонтёром");
        keyboardRows.add(keyboardRow);
        keyboardRow = new KeyboardRow();
        keyboardRow.add("Пройти регистрацию");
        keyboardRow.add("Посмотреть свои данные");
        keyboardRows.add(keyboardRow);
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(keyboardMarkup);
        exec(sendMessage);
    }

    private void catsList(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Котик ищет хозяина\n");
        List<Pet> catsList = petService.getPetListByVariety(cat).stream()
                                       .filter(pet -> pet.getClient() == null)
                                       .toList();
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        for (Pet pet : catsList) {
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            var button = new InlineKeyboardButton();
            button.setText(pet.getName() + " | " + pet.getBirthday().toLocalDate().getMonth() + "." + pet.getBirthday().toLocalDate().getYear());
            button.setCallbackData(pet.getId().toString());
            rowInLine.add(button);
            rowsInLine.add(rowInLine);
        }
        markupInLine.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(markupInLine);
        exec(sendMessage);
    }

    private void dogsList(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Пёсик ищет хозяина\n");
        List<Pet> dogsList = petService.getPetListByVariety(dog).stream()
                                       .filter(pet -> pet.getClient() == null)
                                       .toList();
        System.out.println(dogsList);
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        for (Pet pet : dogsList) {
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            var button = new InlineKeyboardButton();
            button.setText(pet.getName() + " | " + pet.getBirthday().toLocalDate().getMonth() + "." + pet.getBirthday().toLocalDate().getYear());
            button.setCallbackData(pet.getId().toString());
            rowInLine.add(button);
            rowsInLine.add(rowInLine);
        }
        markupInLine.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(markupInLine);
        exec(sendMessage);
    }

    private void getPet(String message, Long chatId) {
        if (StringUtils.isNumeric(message)) {
            Long petId = Long.parseLong(message);
            Pet pet = petService.getById(petId);
            PetAvatar petAvatar = petAvatarService.getPetAvatar(petId);
            if (petAvatar.getId() != null) {
                try {
                    String path = petAvatar.getFilePath();
                    InputFile photo = new InputFile(new File(path));
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId);
                    sendPhoto.setPhoto(photo);
                    sendPhoto.setCaption(pet.getName() + " родился " + pet.getBirthday());
                    sendPhoto.setReplyMarkup(photoMarkup(pet));
                    execute(sendPhoto);
                } catch (TelegramApiException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException();
                }
            } else {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText(pet.getName() + " родился " + pet.getBirthday());
                sendMessage.setReplyMarkup(photoMarkup(pet));
                exec(sendMessage);
            }
        }
    }

    private InlineKeyboardMarkup photoMarkup(Pet pet) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var previousButton = new InlineKeyboardButton();
        var nextButton = new InlineKeyboardButton();
        previousButton.setText("Предыдущий");
        nextButton.setText("Следующий");
        List<Pet> petList = petService.getPetListByVariety(pet.getPetVariety()).stream()
                                      .filter(pet1 -> pet1.getClient() == null)
                                      .toList();
        int petIndex = 0;
        int previousPetIndex = petList.size() - 1;
        int nextPetIndex = 1;
        for (int i = 0; i < petList.size(); i++) {
            if (petList.get(i).equals(pet)) {
                petIndex = i;
                break;
            }
        }
        if (petList.size() > 2) {
            if (petIndex != 0 && petIndex != (petList.size() - 1)) {
                previousPetIndex = petIndex - 1;
                nextPetIndex = petIndex + 1;
            } else if (petIndex == (petList.size() - 1)) {
                previousPetIndex = petIndex - 1;
                nextPetIndex = 0;
            } else if (petIndex == 0) {
                previousPetIndex = petList.size() - 1;
                nextPetIndex = 1;
            }
        } else {
            if (petIndex == 0) {
                previousPetIndex = 1;
                nextPetIndex = 1;
            } else {
                previousPetIndex = 0;
                nextPetIndex = 0;
            }
        }
        Pet previousPet = petList.get(previousPetIndex);
        Pet nextPet = petList.get(nextPetIndex);
        Long previousId = previousPet.getId();
        Long nextId = nextPet.getId();
        previousButton.setCallbackData(previousId.toString());
        nextButton.setCallbackData(nextId.toString());
        rowInLine.add(previousButton);
        rowInLine.add(nextButton);
        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }

    private void callVolunteer(Long chatId, String userName) {
        Collection<Volunteer> volunteerList = volunteerService.getAll();
        for (Volunteer volunteer : volunteerList) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(volunteer.getChatId());
            sendMessage.setText("Клиент @" + userName + " хочет с Вами связаться!");
            exec(sendMessage);
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Наши волонтёры свяжутся с Вами в ближайшее время!");
        exec(sendMessage);
    }

    public void exec(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void registration(Long chatId, String text) {
        Client client = clientService.getByChatId(chatId);
        if (client.getFirstName() != null
                && client.getBirthday() != null
                && client.getAddress() != null
                && client.getLastName() != null
                && client.getPassport() != null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Регистрация уже пройдена!\nДля изменения персональных данных, пожалуйста, свяжитесь с волонтёром!");
            exec(sendMessage);
        } else if (client.getFirstName() == null) {
            if (text == null) {
                registrationStatus.add(chatId);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Введите, пожалуйста, Ваше имя!");
                InlineKeyboardMarkup markupInLine = closeRegistrationMarkup();
                sendMessage.setReplyMarkup(markupInLine);
                exec(sendMessage);
            } else {
                client.setFirstName(text);
                clientService.save(client);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Введите, пожалуйста, Вашу фамилию!");
                InlineKeyboardMarkup markupInLine = closeRegistrationMarkup();
                sendMessage.setReplyMarkup(markupInLine);
                exec(sendMessage);
            }
        } else if (client.getLastName() == null) {
            if (text == null) {
                registrationStatus.add(chatId);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Введите, пожалуйста, Вашу фамилию!");
                InlineKeyboardMarkup markupInLine = closeRegistrationMarkup();
                sendMessage.setReplyMarkup(markupInLine);
                exec(sendMessage);
            } else {
                client.setLastName(text);
                clientService.save(client);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Введите, пожалуйста, Ваш адрес!");
                InlineKeyboardMarkup markupInLine = closeRegistrationMarkup();
                sendMessage.setReplyMarkup(markupInLine);
                exec(sendMessage);
            }
        } else if (client.getAddress() == null) {
            if (text == null) {
                registrationStatus.add(chatId);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Введите, пожалуйста, Ваш адрес!");
                InlineKeyboardMarkup markupInLine = closeRegistrationMarkup();
                sendMessage.setReplyMarkup(markupInLine);
                exec(sendMessage);
            } else {
                client.setAddress(text);
                clientService.save(client);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Введите, пожалуйста, Вашу  дату рождения!\n\nДата рождения вводится в формате: гггг-мм-дд");
                InlineKeyboardMarkup markupInLine = closeRegistrationMarkup();
                sendMessage.setReplyMarkup(markupInLine);
                exec(sendMessage);
            }
        } else if (client.getBirthday() == null) {
            if (text == null) {
                registrationStatus.add(chatId);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Введите, пожалуйста, Вашу  дату рождения!\n\nДата рождения вводится в формате: гггг-мм-дд");
                InlineKeyboardMarkup markupInLine = closeRegistrationMarkup();
                sendMessage.setReplyMarkup(markupInLine);
                exec(sendMessage);
            } else {
                try {
                    client.setBirthday(Date.valueOf(text));
                } catch (RuntimeException e) {
                    log.error(e.getMessage());
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Неправильный ввод даты!");
                    exec(sendMessage);
                    sendMessage = new SendMessage();
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Введите, пожалуйста, Вашу  дату рождения!\n\nДата рождения вводится в формате: гггг-мм-дд");
                    InlineKeyboardMarkup markupInLine = closeRegistrationMarkup();
                    sendMessage.setReplyMarkup(markupInLine);
                    exec(sendMessage);
                }
                clientService.save(client);
                if (client.getBirthday() != null) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Введите, пожалуйста, Ваш паспорт!");
                    InlineKeyboardMarkup markupInLine = closeRegistrationMarkup();
                    sendMessage.setReplyMarkup(markupInLine);
                    exec(sendMessage);
                }
            }
        } else if (client.getPassport() == null) {
            if (text == null) {
                registrationStatus.add(chatId);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Введите, пожалуйста, Ваш паспорт!");
                InlineKeyboardMarkup markupInLine = closeRegistrationMarkup();
                sendMessage.setReplyMarkup(markupInLine);
                exec(sendMessage);
            } else {
                client.setPassport(text);
                clientService.save(client);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Регистрация пройдена!\nДля изменения персональных данных, пожалуйста, свяжитесь с волонтёром!");
                exec(sendMessage);
                registrationStatus.remove(chatId);
            }
        }
    }

    private InlineKeyboardMarkup closeRegistrationMarkup() {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var closeRegistrationButton = new InlineKeyboardButton();
        closeRegistrationButton.setText("Прервать регистрацию");
        closeRegistrationButton.setCallbackData("/close-registration");
        rowInLine.add(closeRegistrationButton);
        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }

    private void registrationStop(Long chatId) {
        registrationStatus.remove(chatId);
        sendMessage(chatId, "Регистрация прервана!");
    }

    private void showData(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        Client client = clientService.getByChatId(chatId);
        String name;
        String surname;
        String address;
        Date birthday;
        String passport;
        if (client.getFirstName() == null) {
            name = "Не введено";
        } else {
            name = client.getFirstName();
        }
        if (client.getLastName() == null) {
            surname = "Не введено";
        } else {
            surname = client.getLastName();
        }
        if (client.getAddress() == null) {
            address = "Не введено";
        } else {
            address = client.getAddress();
        }
        if (client.getBirthday() == null) {
            birthday = Date.valueOf("1900-01-01");
        } else {
            birthday = client.getBirthday();
        }
        if (client.getPassport() == null) {
            passport = "Не введено";
        } else {
            passport = client.getPassport();
        }
        sendMessage.setText("Имя: " + name + "\nФамилия: " + surname + "\nДата рождения: " + birthday + "\nАдрес: " + address + "\nПаспорт: " + passport);
        exec(sendMessage);
    }

    private void dailyReport(Long chatId, String message, byte[] photo) {
        DailyReport dailyReport = new DailyReport(null, photo, message, Date.valueOf(LocalDate.now()), clientService.getByChatId(chatId).getId(), false);
        dailyReportService.saveReport(dailyReport);
        dailyReportStatus.remove(chatId);
        sendMessage(chatId, "Ежедневный отчёт успешно отправлен!");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void dailyForm() {
        schedulerService.updateProbation();
        List<ProbationPeriod> probations = schedulerService.getProbation();
        LocalDate localDate = LocalDate.now();
        Date date = Date.valueOf(localDate);
        for (ProbationPeriod probation : probations) {
            if (probation.getLastDate().after(date)) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(clientService.getById(probation.getClientId()).getChatId());
                sendMessage.setText(infoService.getInfoTextById(17L));
                sendMessage.setReplyMarkup(dailyReportMarkup());
                exec(sendMessage);
            } else if (probation.getLastDate().equals(date)) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(clientService.getById(probation.getClientId()).getChatId());
                sendMessage.setText(infoService.getInfoTextById(18L));
                exec(sendMessage);
            } else {
                schedulerService.deleteProbation(probation.getId());
                petService.delete(probation.getPetId());
                dailyReportService.deleteReports(probation.getClientId());
                Client client = clientService.getById(probation.getClientId());
                if (client.getPets() == null) {
                    clientService.delete(probation.getClientId());
                }
            }
        }
    }

    private InlineKeyboardMarkup dailyReportMarkup() {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var closeRegistrationButton = new InlineKeyboardButton();
        closeRegistrationButton.setText("Отправить отчёт");
        closeRegistrationButton.setCallbackData("Отправить отчёт");
        rowInLine.add(closeRegistrationButton);
        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }

    private void takeReport(Long chatId) {
        dailyReportStatus.add(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Ожидаем Вашего отчёта!");
        exec(sendMessage);
    }

    private void createAnswer(Update update) {
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        if (!dailyReportStatus.contains(chatId)) {
            if (!registrationStatus.contains(chatId)) {
                sendMessage(chatId, "Я Вас не понимаю.\n\nПожалуйста, введите другую команду, воспользуйтесь Menu бота или свяжитесь с волонтёром.");
            } else {
                registration(chatId, message);
            }
        } else {
            dailyReport(chatId, message, null);
        }
    }
}
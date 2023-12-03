package sky.pro.Animals.listener;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sky.pro.Animals.configuration.PetShelterTelegramConfig;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.entity.PetAvatar;
import sky.pro.Animals.entity.Volunteer;
import sky.pro.Animals.service.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static sky.pro.Animals.model.PetVariety.cat;
import static sky.pro.Animals.model.PetVariety.dog;

@Component
@Log4j
public class PetShelterTelegramBot extends TelegramLongPollingBot {
    private final PetServiceImpl petService;
    private final PetAvatarServiceImpl petAvatarService;
    private final ClientServiceImpl clientService;
    private final InfoServiceImpl infoService;
    private final PetShelterTelegramConfig botConfig;
    private final VolunteerServiceImpl volunteerService;

    public PetShelterTelegramBot(PetServiceImpl petService, PetAvatarServiceImpl petAvatarService, ClientServiceImpl clientService, InfoServiceImpl infoService, PetShelterTelegramConfig botConfig, VolunteerServiceImpl volunteerService) {
        this.petService = petService;
        this.petAvatarService = petAvatarService;
        this.clientService = clientService;
        this.infoService = infoService;
        this.botConfig = botConfig;
        this.volunteerService = volunteerService;
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
            System.out.println(message);
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
                default -> {
                    sendMessage(chatId, "Я Вас не понимаю.\n\nПожалуйста, введите другую команду, воспользуйтесь Menu бота или свяжитесь с волонтёром.");
                }
            }
        } else if (update.hasCallbackQuery()) {
            String message = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            System.out.println(message);
            getPet(message, chatId);
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
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(keyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    private void catsList(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Имя                ДР\n");
        List<Pet> catsList = petService.getPetListByVariety(cat);
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
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    private void dogsList(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Имя                ДР\n");
        List<Pet> dogsList = petService.getPetListByVariety(dog);
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
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
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
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    log.error(e.getMessage());
                }
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
        List<Pet> petList = petService.getPetListByVariety(pet.getPetVariety());
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
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Наши волонтёры свяжутся с Вами в ближайшее время!");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
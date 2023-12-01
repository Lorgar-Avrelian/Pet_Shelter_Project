package sky.pro.Animals.listener;

import org.apache.tomcat.util.net.SendfileDataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sky.pro.Animals.configuration.TelegramBotConfig2;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.entity.PetAvatar;
import sky.pro.Animals.model.PetVariety;
import sky.pro.Animals.service.ClientServiceImpl;
import sky.pro.Animals.service.InfoServiceImpl;
import sky.pro.Animals.service.PetAvatarServiceImpl;
import sky.pro.Animals.service.PetServiceImpl;


import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceTelegramBot2 extends TelegramLongPollingBot {
    @Autowired
    private PetServiceImpl petService;
    @Autowired
    private ClientServiceImpl clientService;
    @Autowired
    private InfoServiceImpl infoService;
    @Autowired
    private TelegramBotConfig2 botConfig;
    @Autowired
    private PetAvatarServiceImpl avatarService;


    Logger LOG = LoggerFactory.getLogger(ServiceTelegramBot2.class);
    static final String HELP_TEXT = "Привет,этот бот поможет выбрать животное из приюта.\n\n" +
            "Вы можете выполнять команды из главного меню слева или набрав команду:\n\n" +
            "Команда /start чтобы увидеть приветственное сообщение\n\n" +
            "Команда /mydata увидеть данные, хранящиеся о себе \n\n" +
            "Команда /help чтобы увидеть это сообщение снова\n\n";


    public ServiceTelegramBot2(PetServiceImpl petService, ClientServiceImpl clientService, InfoServiceImpl infoService, TelegramBotConfig2 botConfig) {
        this.petService = petService;
        this.clientService = clientService;
        this.infoService = infoService;
        this.botConfig = botConfig;
        //меню для бота в кострукторе
        List<BotCommand> listOfCommand = new ArrayList<>();
        listOfCommand.add(new BotCommand("/start", "получите приветственное сообщение"));
        listOfCommand.add(new BotCommand("/register", "для регистрации"));
        listOfCommand.add(new BotCommand("/mydata", "получить данные о вас "));
        listOfCommand.add(new BotCommand("/deletedata", "удалить данные о вас"));
        listOfCommand.add(new BotCommand("/help", "информация как пользоваться ботом"));
        listOfCommand.add(new BotCommand("/Приют для кошек", "информация как пользоваться ботом"));
        try {
            this.execute(new SetMyCommands(listOfCommand, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            LOG.error("error setting bot's command list : " + e.getMessage());
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
        infoService.checkInfo();
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();     //сообщение от пользователя
            Long chatId = update.getMessage().getChatId();         //номер чата, для общения именно с этим пользователем
            ReplyKeyboardMarkup replyKeyboardMarkup = null;//кнопки для всех команд
            switch (message) {     //в командах применяем методы которые сами пишем ниже
                case "/start":
                    startCommand(chatId, update.getMessage().getChat().getFirstName());
                    registerUsers(update.getMessage());
                    break;
                case "/Приют для кошек":
                    String c = "Вас приветствует приют для кошек";
                    sendMessage(replyKeyboardMarkup, chatId, c);
                    try {
                        execute(getCat(chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "/Приют для собак":
                    String d = "Вас приветствует приют для собак";
                    sendMessage(replyKeyboardMarkup, chatId, d);
                    try {
                        execute(getDog(chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "/register":
                    buttonsForRegister(chatId);
                    break;
                case "/help":
                    sendMessage(replyKeyboardMarkup, chatId, HELP_TEXT);
                    break;
                default:
                    sendMessage(replyKeyboardMarkup, chatId, ("извините,такой команды пока нет"));
            }
        }
        /**
         Этот else if отлавливает id прозрачных кнопок (Небольшие кирпичи с командами)
         например "YES_BUTTON" это id для кнопки yes. В зависимости от id кнопки возвращает функционал этой кнопки
         */
        else if (update.hasCallbackQuery()) {  //может в update передался id кнопки(yesButton.setCallbackData("YES_BUTTON"))
            String callBackData = update.getCallbackQuery().getData();//получаем запрос,Id нажатой кнопки
            long messageID = update.getCallbackQuery().getMessage().getMessageId();//получаем инфу по message  через update.getCallbackQuery()потомучто в updata сообщения нет
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callBackData.equals("listCat")) {
                PetVariety petVariety = PetVariety.valueOf("cat");
                String p1 = petService.getPetListByVariety(petVariety);
                buttonForListCat(chatId);

                EditMessageText message = new EditMessageText();
                message.setChatId(String.valueOf(chatId));
                message.setText(p1);
                message.setMessageId((int) messageID);//отправляем message с определенным ID
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    LOG.error("Error occurred : " + e.getMessage());
                }
            } else if (callBackData.equals("listDog")) {
                PetVariety petVariety = PetVariety.valueOf("dog");
                String p1 = petService.getPetListByVariety(petVariety);
                buttonForListCat(chatId);

                EditMessageText message = new EditMessageText();
                message.setChatId(String.valueOf(chatId));
                message.setText(p1);
                message.setMessageId((int) messageID);//отправляем message с определенным ID
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    LOG.error("Error occurred : " + e.getMessage());
                }
            } else if (callBackData.equals("Приют для кошек_BUTTON")) {
                String text = "Вы выбрали приют для кошек!";
                EditMessageText message = new EditMessageText();
                message.setChatId(String.valueOf(chatId));
                message.setText(text);
                message.setMessageId((int) messageID);//отправляем message с определенным ID
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    LOG.error("Error occurred : " + e.getMessage());
                }
            } else if (callBackData.equals("Приют для собак_BUTTON")) {
                String text = "Вы выбрали приют для собак!";
                EditMessageText message = new EditMessageText();
                message.setChatId(String.valueOf(chatId));
                message.setText(text);
                message.setMessageId((int) messageID);//отправляем message с определенным ID
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    LOG.error("Error occurred : " + e.getMessage());
                }
            } else if (update.hasCallbackQuery()) {
                String call_data = update.getCallbackQuery().getData();
                long chat_id = update.getCallbackQuery().getMessage().getChatId();
                String path = avatarService.findAvatar(Long.parseLong(call_data, 10)).getFilePath();
                String pet = petService.getById(Long.parseLong(call_data, 10)).toString();
                EditMessageText message = new EditMessageText();
                message.setChatId(String.valueOf(chat_id));
                message.setText(pet);
                message.setMessageId((int) messageID);//отправляем message с определенным ID
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    LOG.error("Error occurred : " + e.getMessage());
                }
                try {
                    execute(getPhoto(chat_id, Long.parseLong(call_data, 10)));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }


            }
        }
    }

    private void buttonForListCat(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите питомца и введите ID питомца для просмотра более детальной информации");
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        var ageButton1 = new InlineKeyboardButton();
        ageButton1.setText("Выбрать =>");
        ageButton1.setCallbackData("listCat");
        rowInline1.add(ageButton1);
        rowsInLine.add(rowInline1);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOG.error("Error occurred : " + e.getMessage());
        }
    }

    private void buttonForListDog(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите питомца и введите ID питомца для просмотра более детальной информации");
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        var ageButton2 = new InlineKeyboardButton();
        ageButton2.setText("Выбрать =>");
        ageButton2.setCallbackData("listDog");
        rowInline1.add(ageButton2);
        rowsInLine.add(rowInline1);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOG.error("Error occurred : " + e.getMessage());
        }
    }

    /**
     * Аналог метода {@link ServiceTelegramBot2#buttonForShelters(Long chatId)}
     *
     * @param chatId
     */
    private void buttonsForRegister(Long chatId) { //прозрачные кнопки с сообщением (не клавиатура)
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Вы действительно хотите зарегистрироваться? ");
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();//класс для создания прозрачной кнопки под сообщением
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();//список списков для хранения кнопок
        List<InlineKeyboardButton> rowInline = new ArrayList<>();//список с кнопками для ряда
        var yesButton = new InlineKeyboardButton();
        yesButton.setText("Yes");
        yesButton.setCallbackData("YES_BUTTON");//индификатор кнопки (позволяет понять боту ,какая кнопка была нажата)
        var noButton = new InlineKeyboardButton();
        noButton.setText("No");
        noButton.setCallbackData("NO_BUTTON");
        rowInline.add(yesButton); //добавили кнопки в список для ряда
        rowInline.add(noButton);
        rowsInLine.add(rowInline); //добавили список с кнопками для ряда в список для хранения кнопок
        markupInLine.setKeyboard(rowsInLine);// в классе меняем значения для кнопки
        message.setReplyMarkup(markupInLine);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOG.error("Error occurred : " + e.getMessage());
        }
    }

    /**
     * Метод для записи в таблицу новых клиентов <br>
     * При помощи {@link sky.pro.Animals.repository.ClientRepository#findByChatId(Long)} проверяем на наличие id чата. <br>
     * При помощи {@link sky.pro.Animals.repository.ClientRepository#save(Object)} сохраняем клиента в таблицу Client. <br>
     *
     * @param message
     * @see sky.pro.Animals.repository.ClientRepository#findByChatId(Long)
     * @see sky.pro.Animals.repository.ClientRepository#save(Object)
     */
    private void registerUsers(Message message) {
        if (clientService.getByChatId(message.getChatId()) == null) {//если чатайди не найден
            Long chatId = message.getChatId();                       //то нужно создать новый
            var chat = message.getChat();
            Client newClient = new Client();
            newClient.setChatId(chatId);
            newClient.setFirstName(chat.getFirstName());
            newClient.setLastName(chat.getLastName());
            newClient.setUserName(chat.getUserName());
            clientService.save(newClient);
            LOG.info("client saved : " + newClient);
        }
    }

    /**
     * Метод для команды старт.
     * Переменная {@code ReplyKeyboardMarkup rep = buttonsForStart();} инициализируется
     * методом {@link ServiceTelegramBot2#buttonsForStart()}
     *
     * @param chatId
     * @param name
     */
    private void startCommand(Long chatId, String name) {
        String answer = "Привет, " + name + ", какой приют вы хотите выбрать?";
        LOG.info("Replied to user " + name);
        ReplyKeyboardMarkup rep = buttonsForStart();
        sendMessage(rep, chatId, answer);
    }

    /**
     * Метод создаёт прозрачные кнопки под сообщением.
     * Используется класс {@code InlineKeyboardButton()}для создания объекта прозрачной кнопки.
     * При помощи метода {@code setCallbackData()  } назначаем ID для кнопки, что бы в дальнейшем боту было
     * понятно какую кнопку выбрал пользователь.
     * В коллекцию {@code rowInline } добавляем кнопки для ряда.
     * В коллекцию {@code rowsInLine} добавляем коллекцию с кнопками для ряда {@code rowInline }
     * Не путайте {@code rowsInLine} и {@code rowInline }.
     * Далее {@code markupInLine.setKeyboard(rowsInLine)} : в объекте класса {@code InlineKeyboardMarkup}
     * инициализируем поле keyboard коллекцией {@code rowsInLine} : {@code markupInLine.setKeyboard(rowsInLine)}.
     * {@code message.setReplyMarkup(markupInLine)}: инициализируем поле replyMarkup класса SendMessage,добавляем
     * созданную ренее клавиатур markupInLine.
     *
     * @param chatId
     */
    private void buttonForShelters(Long chatId) { //создаем прозрачные кнопки с сообщением (не клавиатура)
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Какой приют вы хотите выбрать??? ");
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();//класс для создания прозрачной кнопки под сообщением
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();//список списков для хранения кнопок
        List<InlineKeyboardButton> rowInline = new ArrayList<>();//список с кнопками для ряда
        var catButton = new InlineKeyboardButton();
        catButton.setText("Приют для кошек");
        catButton.setCallbackData("Приют для кошек_BUTTON");//индификатор кнопки (позволяет понять боту ,какая кнопка была нажата)
        var dogButton = new InlineKeyboardButton();
        dogButton.setText("Приют для собак");
        dogButton.setCallbackData("Приют для собак_BUTTON");
        rowInline.add(catButton); //добавили кнопки в список для ряда
        rowInline.add(dogButton);
        rowsInLine.add(rowInline); //добавили список с кнопками для ряда в список для хранения кнопок
        markupInLine.setKeyboard(rowsInLine);// в классе меняем значения для кнопки
        message.setReplyMarkup(markupInLine);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOG.error("Error occurred : " + e.getMessage());
        }
    }

    /**
     * В методе описаны не прозрачные кнопки(клавиатура) для команды старт.
     * Создаём объект: {@code ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup() } для
     * разметки клавиатуры.
     * Создаем коллекцию {@code List<KeyboardRow> keyboardRows = new ArrayList<>()} для кнопок.
     * Создаем объект класса KeyboardRow : {@code KeyboardRow row = new KeyboardRow()}.
     * Добавляем кнопки в наш ряд: {@code row.add("/Приют для кошек")} при помощи метода add класса KeyboardRow
     * {@link KeyboardRow#add(String)}.
     * Добавляем ряды в коллекцию: {@code keyboardRows.add(row)}.
     * У класса replyKeyboardMarkup инициализируем поле keyboard коллекцией keyboardRows :
     * {@code replyKeyboardMarkup.setKeyboard(keyboardRows)}
     *
     * @return
     */

    private ReplyKeyboardMarkup buttonsForStart() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();//разметка для клавиатуры}
        List<KeyboardRow> keyboardRows = new ArrayList<>(); //список из рядов(ряд в который добавляем кнопки
        KeyboardRow row = new KeyboardRow();//ряд1
//        var cetButton = new InlineKeyboardButton();
        row.add("/Приют для кошек");
//        cetButton.setCallbackData("Приют для кошек_BUTTON");
        row.add("/Приют для собак");
//        cetButton.setCallbackData("Приют для собак_BUTTON");
        keyboardRows.add(row);      //ряд 1 добавили
//            row = new KeyboardRow();//ряд2
//
//            row.add("ppppppp ");
//            row.add("прислать что нибудь [jhjitt");
//            row.add("прислать что nj [jhjitt");
//
//            keyboardRows.add(row);          //ряд 2 добавили
        replyKeyboardMarkup.setKeyboard(keyboardRows);//добавляем лист с рядами в метод для разметки
//        message.setReplyMarkup(replyKeyboardMarkup);
        return replyKeyboardMarkup;
    }

    private void sendMessage(ReplyKeyboardMarkup replyKeyboardMarkup, Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOG.error("Error occurred : " + e.getMessage());
        }
    }


    private ReplyKeyboardMarkup buttonsForDefault() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();//разметка для клавиатуры}
        List<KeyboardRow> keyboardRows = new ArrayList<>(); //список из рядов(ряд в который добавляем кнопки
        KeyboardRow row = new KeyboardRow();//ряд1
        row.add("проверка 1");
        row.add("проверка 2");
        keyboardRows.add(row);      //ряд 1 добавили
        replyKeyboardMarkup.setKeyboard(keyboardRows);//добавляем лист с рядами в метод для разметки
//        message.setReplyMarkup(replyKeyboardMarkup);
        return replyKeyboardMarkup;
    }

    //========================================================================
    public SendMessage getCat(long chat_id) {

        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Выберите питомца");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (Pet e : petService.getCat()
        ) {
            List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText(e.toString());
            inlineKeyboardButton1.setCallbackData(e.getId().toString());
            rowInline1.add(inlineKeyboardButton1);
            rowsInline.add(rowInline1);
        }

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        return message;

    }

    public SendMessage getDog(long chat_id) {

        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Выберите питомца");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (Pet e : petService.getDog()
        ) {
            List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText(e.toString());
            inlineKeyboardButton1.setCallbackData(e.getId().toString());
            rowInline1.add(inlineKeyboardButton1);
            rowsInline.add(rowInline1);
        }

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        return message;

    }

    public SendMessage getPhoto(long chat_id, Long id) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Вы можете просмотреть фото питомца");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Посмотреть фото");
        inlineKeyboardButton1.setCallbackData(id.toString());
        rowInline1.add(inlineKeyboardButton1);
        rowsInline.add(rowInline1);


        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        return message;
    }
}

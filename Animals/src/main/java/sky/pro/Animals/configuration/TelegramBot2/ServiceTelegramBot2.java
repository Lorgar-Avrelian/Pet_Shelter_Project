package sky.pro.Animals.configuration.TelegramBot2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.repository.ClientRepository;


import java.util.ArrayList;
import java.util.List;
@Component
public class ServiceTelegramBot2 extends TelegramLongPollingBot{

    @Autowired
   private ClientRepository clientRepository;
        Logger LOG = LoggerFactory.getLogger(ServiceTelegramBot2.class);
    static final String HELP_TEXT = "Привет,этот бот поможет выбрать животное из приюта.\n\n" +
            "Вы можете выполнять команды из главного меню слева или набрав команду:\n\n" +
            "Команда /start чтобы увидеть приветственное сообщение\n\n" +
            "Команда /mydata увидеть данные, хранящиеся о себе \n\n" +
            "Команда /help чтобы увидеть это сообщение снова\n\n";

        private TelegramBotConfig2 botConfig;

        public ServiceTelegramBot2(TelegramBotConfig2 botConfig) {
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
            if (update.hasMessage() && update.getMessage().hasText()) {//если есть сообщение и в сообщениии есть текст
                String message = update.getMessage().getText();     //сообщение от пользователя
                Long chatId = update.getMessage().getChatId();         //номер чата ,для общения именно с этим пользователем
                ReplyKeyboardMarkup replyKeyboardMarkup = null;//кнопки для всех команд


                switch (message) {     //применяем методы которые сами пишем ниже
                    case "/start":

                        startCommand(chatId, update.getMessage().getChat().getFirstName());
                        registerUsers(update.getMessage());


                        break;

                    case "/Приют для кошек":

                        String c = "вы попали в кошачий приют";
                        sendMessage(replyKeyboardMarkup,chatId,c);
                        break;

                    case "/Приют для собак":
                        String d = "вы попали в собачий приют";
                        sendMessage(replyKeyboardMarkup,chatId,d);
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
            /*
            этот else if отлавливает id прозрачных кнопок (Не большие кирпичи с командами)
            например "YES_BUTTON" это id для кнопки yes.В зависимости от id кнопки возвращает функционал этой кнопки
             */
            else if (update.hasCallbackQuery()) {  //может в update передался id кнопки(yesButton.setCallbackData("YES_BUTTON"))
                String callBackData = update.getCallbackQuery().getData();//получаем запрос,Id нажатой кнопки

                long messageID = update.getCallbackQuery().getMessage().getMessageId();//получаем инфу по message  через update.getCallbackQuery()потомучто в updata сообщения нет
                long chatId = update.getCallbackQuery().getMessage().getChatId();

                if (callBackData.equals("YES_BUTTON")) {
                    String text = "you pressed YES Button";
                    EditMessageText message = new EditMessageText();
                    message.setChatId(String.valueOf(chatId));
                    message.setText(text);
                    message.setMessageId((int)messageID);//отправляем message с определенным ID
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        LOG.error("Error occurred : " + e.getMessage());

                    }

                } else if (callBackData.equals("NO_BUTTON")) {
                    String text = "you pressed NO Button";
                    EditMessageText message = new EditMessageText();
                    message.setChatId(String.valueOf(chatId));
                    message.setText(text);
                    message.setMessageId((int)messageID);//отправляем message с определенным ID
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        LOG.error("Error occurred : " + e.getMessage());

                    }
                }
                else if (callBackData.equals("Приют для кошек_BUTTON")) {
                    String text = "Вы выбрали кошачий приют!";
                    EditMessageText message = new EditMessageText();
                    message.setChatId(String.valueOf(chatId));
                    message.setText(text);
                    message.setMessageId((int) messageID);//отправляем message с определенным ID
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        LOG.error("Error occurred : " + e.getMessage());

                    }
                }
                    else if (callBackData.equals("Приют для собак_BUTTON")) {
                        String text = "Вы выбрали собачий приют!";
                        EditMessageText message = new EditMessageText();
                        message.setChatId(String.valueOf(chatId));
                        message.setText(text);
                        message.setMessageId((int)messageID);//отправляем message с определенным ID
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            LOG.error("Error occurred : " + e.getMessage());

                        }





                }}


        }

    /**
     * Аналог метода {@link ServiceTelegramBot2#buttonForShelters(Long chatId)}
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

        private void registerUsers(Message message) {
            if (clientRepository.findById(message.getChatId()).isEmpty()) {//если чатайди пуст
                Long chatId = message.getChatId();                       //то нужно создать новый
                var chat = message.getChat();
                Client newUser = new Client();

                newUser.setChatId(chatId);
                newUser.setFirstName(chat.getFirstName());
                newUser.setLastName(chat.getLastName());
                newUser.setUserName(chat.getUserName());
//                newUser.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
                clientRepository.save(newUser);
                LOG.info("user saved : " + newUser);

            }
        }

    /**
     * Метод для команды старт.
     * Переменная {@code ReplyKeyboardMarkup rep = buttonsForStart();} инициализируется
     * методом {@link ServiceTelegramBot2#buttonsForStart()}
     * @param chatId
     * @param name
     */
        private void startCommand(Long chatId, String name) {


        String answer = "Привет," + name + ",какой приют вы хотите выбрать?";
            LOG.info("Replied to user " + name);

            ReplyKeyboardMarkup rep = buttonsForStart();

            sendMessage(rep, chatId, answer);

        }

    /**
     * Метод создаёт прозрачные кнопки под сообщением.
     * Используется класс {@code InlineKeyboardButton()}для создания объекта прозрачной кнопки.
     *При помощи метода {@code setCallbackData()  } назначаем ID для кнопки, что бы в дальнейшем боту было
     * понятно какую кнопку выбрал пользователь.
     * В коллекцию {@code rowInline } добавляем кнопки для ряда.
     * В коллекцию {@code rowsInLine} добавляем коллекцию с кнопками для ряда {@code rowInline }
     * Не путайте {@code rowsInLine} и {@code rowInline }.
     * Далее {@code markupInLine.setKeyboard(rowsInLine)} : в объекте класса {@code InlineKeyboardMarkup}
     *инициализируем поле keyboard коллекцией {@code rowsInLine} : {@code markupInLine.setKeyboard(rowsInLine)}.
     *{@code message.setReplyMarkup(markupInLine)}: инициализируем поле replyMarkup класса SendMessage,добавляем
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

    }

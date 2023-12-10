package sky.pro.Animals.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import sky.pro.Animals.entity.Client;
import sky.pro.Animals.entity.DailyReport;
import sky.pro.Animals.entity.DailyReportList;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.listener.PetShelterTelegramBot;
import sky.pro.Animals.service.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * Controller for CRUD operations with clients data
 * <p>
 * <hr>
 * <p>
 * Контроллер для CRUD операций с данными клиентов
 */
@RestController
@Log4j
@RequestMapping(path = "/client")
public class ClientController {
    private final ClientServiceImpl clientService;
    private final InfoServiceImpl infoService;
    private final PetServiceImpl petService;
    private final ProbationPeriodServiceImpl probationPeriodService;
    private final DailyReportServiceImpl dailyReportService;
    private final PetShelterTelegramBot telegramBot;

    public ClientController(ClientServiceImpl clientService,
                            InfoServiceImpl infoService,
                            PetServiceImpl petService,
                            ProbationPeriodServiceImpl probationPeriodService,
                            DailyReportServiceImpl dailyReportService,
                            PetShelterTelegramBot telegramBot) {
        this.clientService = clientService;
        this.infoService = infoService;
        this.petService = petService;
        this.probationPeriodService = probationPeriodService;
        this.dailyReportService = dailyReportService;
        this.telegramBot = telegramBot;
    }

    /**
     * API for getting collection with all clients. <br>
     * Used service method {@link ClientService#getAll()}. <br>
     * <hr>
     * API для получения коллекции, содержащей всех клиентов. <br>
     * Использован метод сервиса {@link ClientService#getAll()}. <br>
     * <hr>
     *
     * @return Collection with all clients / Коллекцию со всеми клиентами
     * @see ClientService#getAll()
     */
    @GetMapping(path = "/get")
    public ResponseEntity<Collection<Client>> getAllClients() {
        infoService.checkInfo();
        Collection<Client> clients = clientService.getAll();
        if (clients == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(clients);
        }
    }

    /**
     * API for getting client with this id. <br>
     * Used service method {@link ClientService#getById(Long)}. <br>
     * <hr>
     * API для получения клиента с конкретным id. <br>
     * Использован метод сервиса {@link ClientService#getById(Long)}. <br>
     * <hr>
     *
     * @param id
     * @return Client with this id if exist / Клиента, если таковой существует
     * @see ClientService#getById(Long)
     */
    @GetMapping(path = "/get/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        infoService.checkInfo();
        Client client = clientService.getById(id);
        if (client == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(client);
        }
    }

    /**
     * API for saving client with this params. <br>
     * Used service methods {@link ClientService#save(Client)} {@link PetService#save(Pet)}. <br>
     * <hr>
     * API для сохранения клиента с такими параметрами. <br>
     * Использованы методы сервисов {@link ClientService#save(Client)} {@link PetService#save(Pet)}. <br>
     * <hr>
     *
     * @param id
     * @param firstName
     * @param lastName
     * @param userName
     * @param address
     * @param birthday
     * @param passport
     * @param chatId
     * @param firstPetId
     * @param secondPetId
     * @param thirdPetId
     * @return Created client / Созданного клиента
     * @see ClientService#save(Client)
     * @see PetService#save(Pet)
     */
    @PostMapping(path = "/write")
    public ResponseEntity<Client> writeClient(@RequestParam Long id,
                                              @RequestParam String firstName,
                                              @RequestParam String lastName,
                                              @RequestParam String userName,
                                              @RequestParam String address,
                                              @RequestParam Date birthday,
                                              @RequestParam String passport,
                                              @RequestParam Long chatId,
                                              @RequestParam(required = false) Long firstPetId,
                                              @RequestParam(required = false) Long secondPetId,
                                              @RequestParam(required = false) Long thirdPetId
    ) {
        infoService.checkInfo();
        Collection<Pet> clientPets = new ArrayList<>();
        if (firstPetId == null) {
            clientPets = null;
        } else {
            if (petService.getById(firstPetId) != null) {
                clientPets.add(petService.getById(firstPetId));
            }
        }
        if (secondPetId != null) {
            if (petService.getById(secondPetId) != null) {
                clientPets.add(petService.getById(secondPetId));
            }
        }
        if (thirdPetId != null) {
            if (petService.getById(thirdPetId) != null) {
                clientPets.add(petService.getById(thirdPetId));
            }
        }
        Client client = new Client(id, firstName, lastName, userName, address, birthday, passport, chatId, clientPets);
        if (clientPets != null) {
            for (Pet pet : clientPets) {
                pet.setClient(client);
                petService.save(pet);
            }
        }
        Client savedClient = clientService.save(client);
        if (savedClient == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(savedClient);
        }
    }

    /**
     * API for edit client with this params. <br>
     * Used service methods {@link ClientService#save(Client)} {@link PetService#save(Pet)}. <br>
     * <hr>
     * API для редактирования клиента с такими параметрами. <br>
     * Использованы методы сервисов {@link ClientService#save(Client)} {@link PetService#save(Pet)}. <br>
     * <hr>
     *
     * @param id
     * @param firstName
     * @param lastName
     * @param userName
     * @param address
     * @param birthday
     * @param passport
     * @param chatId
     * @param firstPetId
     * @param secondPetId
     * @param thirdPetId
     * @return Edited client / Отредактированного клиента
     * @see ClientService#save(Client)
     * @see PetService#save(Pet)
     */
    @PutMapping(path = "/edit")
    public ResponseEntity<Client> editClient(@RequestParam Long id,
                                             @RequestParam String firstName,
                                             @RequestParam String lastName,
                                             @RequestParam String userName,
                                             @RequestParam String address,
                                             @RequestParam Date birthday,
                                             @RequestParam String passport,
                                             @RequestParam Long chatId,
                                             @RequestParam(required = false) Long firstPetId,
                                             @RequestParam(required = false) Long secondPetId,
                                             @RequestParam(required = false) Long thirdPetId) {
        Collection<Pet> clientPets = new ArrayList<>();
        if (firstPetId == null) {
            clientPets = null;
        } else {
            if (petService.getById(firstPetId) != null) {
                clientPets.add(petService.getById(firstPetId));
            }
        }
        if (secondPetId != null) {
            if (petService.getById(secondPetId) != null) {
                clientPets.add(petService.getById(secondPetId));
            }
        }
        if (thirdPetId != null) {
            if (petService.getById(thirdPetId) != null) {
                clientPets.add(petService.getById(thirdPetId));
            }
        }
        Client client = new Client(id, firstName, lastName, userName, address, birthday, passport, chatId, clientPets);
        if (clientPets != null) {
            for (Pet pet : clientPets) {
                pet.setClient(client);
                petService.save(pet);
            }
        }
        Client editedClient = clientService.save(client);
        if (editedClient == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(editedClient);
        }
    }

    /**
     * API for removing client with this id. <br>
     * Used service methods {@link ClientService#delete(Long)} {@link PetService#save(Pet)}. <br>
     * <hr>
     * API для удаления клиента с данным id. <br>
     * Использованы методы сервисов {@link ClientService#delete(Long)} {@link PetService#save(Pet)}. <br>
     * <hr>
     *
     * @param id
     * @return Removed client / Удаленного клиента
     * @see ClientService#delete(Long)
     * @see PetService#save(Pet)
     */
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Client> deleteClient(@PathVariable Long id) {
        Collection<Pet> clientPets = clientService.getById(id).getPets();
        for (Pet pet : clientPets) {
            pet.setClient(null);
            petService.save(pet);
        }
        Client deletedClient = clientService.delete(id);
        if (deletedClient == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(deletedClient);
        }
    }

    /**
     * API for adding days by probation period of client. <br>
     * Used service method {@link ProbationPeriodService#changeLastDay(Client, int)}. <br>
     * <hr>
     * API для добавления дней к испытательному сроку клиента. <br>
     * Использован метод сервиса {@link ProbationPeriodService#changeLastDay(Client, int)}. <br>
     * <hr>
     *
     * @param id
     * @param days
     * @return Client with this id if exist / Клиента, если таковой существует
     * @see ProbationPeriodService#changeLastDay(Client, int)
     */
    @PutMapping(path = "/add/{days}")
    public ResponseEntity<Client> addDays(@RequestParam Long id, @RequestParam @PathVariable int days) {
        Client editedClient;
        try {
            editedClient = clientService.getById(id);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).build();
        }
        if (editedClient.getPets() == null) {
            return ResponseEntity.status(400).build();
        }
        editedClient = probationPeriodService.changeLastDay(editedClient, days);
        if (editedClient == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(editedClient);
        }
    }

    /**
     * API for looking saved reports list. <br>
     * Used service method {@link DailyReportService#getAll()}. <br>
     * <hr>
     * API для просмотра списка сохранённых отчётов. <br>
     * Использован метод сервиса {@link DailyReportService#getAll()}. <br>
     * <hr>
     *
     * @return List with all saved daily reports / Список со всеми сохранёнными ежедневными отчётами
     * @see DailyReportService#getAll()
     */
    @GetMapping(path = "/reports")
    public ResponseEntity<Collection<DailyReportList>> getAll() {
        Collection<DailyReportList> dailyReports = dailyReportService.getAll();
        if (dailyReports == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(dailyReports);
        }
    }

    /**
     * API for looking all daily reports of client with this id. <br>
     * Used service method {@link DailyReportService#getAllClientReports(Long)}. <br>
     * <hr>
     * API для просмотра всех ежедневных отчётов клиента с данным id. <br>
     * Использован метод сервиса {@link DailyReportService#getAllClientReports(Long)}. <br>
     * <hr>
     *
     * @param id
     * @return HashMap with client reports list / HashMap со списком ежедневных отчётов клиента
     * @see DailyReportService#getAllClientReports(Long)
     */
    @GetMapping(path = "/reports/{id}")
    public ResponseEntity<HashMap<Long, Date>> getClientReports(@RequestParam @PathVariable Long id) {
        HashMap<Long, Date> dailyReports = dailyReportService.getAllClientReports(id);
        if (dailyReports == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(dailyReports);
        }
    }

    /**
     * API for looking daily report with this id. <br>
     * Used service method {@link DailyReportService#getReport(Long)}. <br>
     * <hr>
     * API для просмотра отчёта с данным id. <br>
     * Использован метод сервиса {@link DailyReportService#getReport(Long)}. <br>
     * <hr>
     *
     * @param id
     * @return Daily report / Ежедневный отчёт
     * @see DailyReportService#getReport(Long)
     */
    @GetMapping(path = "/report/{id}")
    public ResponseEntity<DailyReport> getClientReport(@RequestParam @PathVariable Long id) {
        DailyReport dailyReport = dailyReportService.getReport(id);
        if (dailyReport == null) {
            return ResponseEntity.status(400).build();
        } else {
            dailyReport.setBrows(true);
            dailyReportService.saveReport(dailyReport);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.ok().headers(headers).body(dailyReport);
        }
    }

    /**
     * API for client's warning by volunteer. <br>
     * Used telegram bot method {@link PetShelterTelegramBot#exec(SendMessage)}. <br>
     * <hr>
     * API для предупреждения клиента волонтёром. <br>
     * Использован метод телеграм бота {@link PetShelterTelegramBot#exec(SendMessage)}. <br>
     * <hr>
     *
     * @param id
     * @return ResponseEntity status of warning send / ResponseEntity статус отправки предупреждения
     * @see PetShelterTelegramBot#exec(SendMessage)
     */
    @GetMapping(path = "/warning/{id}")
    public ResponseEntity warning(@RequestParam @PathVariable Long id) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(clientService.getById(id).getChatId());
        sendMessage.setText("Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. \n\nПожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного.");
        telegramBot.exec(sendMessage);
        return ResponseEntity.ok().build();
    }
}
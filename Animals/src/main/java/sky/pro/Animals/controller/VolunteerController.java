package sky.pro.Animals.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.Animals.entity.Volunteer;
import sky.pro.Animals.service.InfoServiceImpl;
import sky.pro.Animals.service.VolunteerService;
import sky.pro.Animals.service.VolunteerServiceImpl;

import java.sql.Date;
import java.util.Collection;

/**
 * Controller for CRUD operations with volunteers data
 * <p>
 * <hr>
 * <p>
 * Контроллер для CRUD операций с данными волонтеров
 */
@RestController
@RequestMapping(path = "/volunteer")
public class VolunteerController {
    private final VolunteerServiceImpl volunteerService;
    private final InfoServiceImpl infoService;

    public VolunteerController(VolunteerServiceImpl volunteerService, InfoServiceImpl infoService) {
        this.volunteerService = volunteerService;
        this.infoService = infoService;
    }

    /**
     * API for getting collection with all volunteers. <br>
     * Used service method {@link VolunteerService#getAll()}. <br>
     * <hr>
     * API для получения коллекции со всеми волонтёрами. <br>
     * Использован метод сервиса {@link VolunteerService#getAll()}. <br>
     * <hr>
     *
     * @return Collection with all volunteers / Коллекция со всеми волонтёрами
     * @see VolunteerService#getAll()
     */
    @GetMapping(path = "/get")
    public ResponseEntity<Collection<Volunteer>> getAllVolunteers() {
        infoService.checkInfo();
        Collection<Volunteer> volunteers = volunteerService.getAll();
        if (volunteers == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(volunteers);
        }
    }

    /**
     * API for getting volunteer with this id. <br>
     * Used service method {@link VolunteerService#getById(Long)}. <br>
     * <hr>
     * API для получения волонтёра с данным id. <br>
     * Использован метод сервиса {@link VolunteerService#getById(Long)}. <br>
     * <hr>
     *
     * @param id
     * @return Volunteer if exist / Волонтёра, если таковой существует
     * @see VolunteerService#getById(Long)
     */
    @GetMapping(path = "/get/{id}")
    public ResponseEntity<Volunteer> getVolunteer(@PathVariable Long id) {
        infoService.checkInfo();
        Volunteer volunteer = volunteerService.getById(id);
        if (volunteer == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(volunteer);
        }
    }

    /**
     * API for saving volunteer with this params. <br>
     * Used service method {@link VolunteerService#save(Volunteer)}. <br>
     * <hr>
     * API для сохранения волонтёра с данными параметрами. <br>
     * Использован метод сервиса {@link VolunteerService#save(Volunteer)}. <br>
     * <hr>
     *
     * @param id
     * @param fio
     * @param address
     * @param birthday
     * @param passport
     * @param chatId
     * @param workPosition
     * @return Saved volunteer / Сохранённый волонтёр
     * @see VolunteerService#save(Volunteer)
     */
    @PostMapping(path = "/write")
    public ResponseEntity<Volunteer> writeVolunteer(@RequestParam Long id,
                                                    @RequestParam String fio,
                                                    @RequestParam String address,
                                                    @RequestParam Date birthday,
                                                    @RequestParam String passport,
                                                    @RequestParam Long chatId,
                                                    @RequestParam String workPosition) {
        infoService.checkInfo();
        Volunteer volunteer = new Volunteer(id, fio, address, birthday, passport, chatId, workPosition);
        Volunteer savedVolunteer = volunteerService.save(volunteer);
        if (savedVolunteer == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(savedVolunteer);
        }
    }

    /**
     * API for editing volunteer with this params. <br>
     * Used service method {@link VolunteerService#save(Volunteer)}. <br>
     * <hr>
     * API для редактирования волонтёра с данными параметрами. <br>
     * Использован метод сервиса {@link VolunteerService#save(Volunteer)}. <br>
     * <hr>
     *
     * @param id
     * @param fio
     * @param address
     * @param birthday
     * @param passport
     * @param chatId
     * @param workPosition
     * @return Edited volunteer / Отредактированный волонтёр
     * @see VolunteerService#save(Volunteer)
     */
    @PutMapping(path = "/edit")
    public ResponseEntity<Volunteer> editVolunteer(@RequestParam Long id,
                                                   @RequestParam String fio,
                                                   @RequestParam String address,
                                                   @RequestParam Date birthday,
                                                   @RequestParam String passport,
                                                   @RequestParam Long chatId,
                                                   @RequestParam String workPosition) {
        infoService.checkInfo();
        Volunteer volunteer = new Volunteer(id, fio, address, birthday, passport, chatId, workPosition);
        Volunteer editedVolunteer = volunteerService.save(volunteer);
        if (editedVolunteer == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(editedVolunteer);
        }
    }

    /**
     * API for deleting volunteer with this id. <br>
     * Used service method {@link VolunteerService#delete(Long)}. <br>
     * <hr>
     * API для удаления волонтёра с данным id. <br>
     * Использован метод сервиса {@link VolunteerService#delete(Long)}. <br>
     * <hr>
     *
     * @param id
     * @return Deleted volunteer / Удалённый волонтёр
     * @see VolunteerService#delete(Long)
     */
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Volunteer> deleteVolunteer(@PathVariable Long id) {
        infoService.checkInfo();
        Volunteer deletedVolunteer = volunteerService.delete(id);
        if (deletedVolunteer == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(deletedVolunteer);
        }
    }
}

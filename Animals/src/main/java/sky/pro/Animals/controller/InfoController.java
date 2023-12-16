package sky.pro.Animals.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.Animals.entity.Info;
import sky.pro.Animals.service.InfoService;
import sky.pro.Animals.service.InfoServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping(path = "/info")
public class InfoController {
    private final InfoServiceImpl infoService;

    public InfoController(InfoServiceImpl infoService) {
        this.infoService = infoService;
    }

    /**
     * API for getting all saved standard info list. <br>
     * Used service method {@link InfoService#getAllInfo()}. <br>
     * <hr>
     * API для получения списка всех сохранённых стандартных сообщений. <br>
     * Использован метод сервиса {@link InfoService#getAllInfo()}. <br>
     * <hr>
     *
     * @return Collection with all standard info / Коллекцию со всеми стандартными сообщениями
     * @see InfoService#getAllInfo()
     */
    @GetMapping()
    public ResponseEntity<Collection<Info>> getAll() {
        Collection<Info> info = infoService.getAllInfo();
        if (info == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(info);
        }
    }

    /**
     * API for edition of standard ifo with this id. <br>
     * Used service method {@link InfoService#editInfo(Info)}. <br>
     * <hr>
     * API для редактирования стандартного сообщения с данным id. <br>
     * Использован метод сервиса {@link InfoService#editInfo(Info)}. <br>
     * <hr>
     *
     * @param id
     * @param editedText
     * @return Edited info / Отредактированную информацию
     * @see InfoService#editInfo(Info)
     */
    @PostMapping(path = "/edit")
    public ResponseEntity<String> editInfo(@RequestParam Long id, @RequestParam String editedText) {
        Info info = infoService.getById(id);
        info.setText(editedText);
        String result = infoService.editInfo(info);
        if (result == null) {
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.ok().body(result);
        }
    }
}

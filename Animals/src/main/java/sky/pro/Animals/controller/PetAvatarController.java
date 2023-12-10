package sky.pro.Animals.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sky.pro.Animals.entity.PetAvatar;
import sky.pro.Animals.service.PetAvatarServiceImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping(path = "/pet-avatar")
public class PetAvatarController {
    private final PetAvatarServiceImpl petAvatarService;

    public PetAvatarController(PetAvatarServiceImpl petAvatarService) {
        this.petAvatarService = petAvatarService;
    }

    /**
     * API for getting avatar of pet with this id. <br>
     * Used service method {@link sky.pro.Animals.service.PetAvatarService#findAvatar(Long)}. <br>
     * <hr>
     * API для получения аватара питомца с данным id. <br>
     * Использован метод сервиса {@link sky.pro.Animals.service.PetAvatarService#findAvatar(Long)}. <br>
     * <hr>
     *
     * @param id
     * @param response
     * @throws IOException
     * @see sky.pro.Animals.service.PetAvatarService#findAvatar(Long)
     */
    @GetMapping(path = "/{id}")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        PetAvatar petAvatar = petAvatarService.findAvatar(id);
        Path avatarPath = Path.of(petAvatar.getFilePath());
        try (
                InputStream is = Files.newInputStream(avatarPath);
                OutputStream os = response.getOutputStream()
        ) {
            response.setContentType(petAvatar.getMediaType());
            response.setContentLength((int) petAvatar.getFileSize());
            is.transferTo(os);
        }
    }

    /**
     * API for getting preview of avatar of pet with this id. <br>
     * Used service method {@link sky.pro.Animals.service.PetAvatarService#findAvatar(Long)}. <br>
     * <hr>
     * API для получения превью аватара питомца с данным id. <br>
     * Использован метод сервиса {@link sky.pro.Animals.service.PetAvatarService#findAvatar(Long)}. <br>
     * <hr>
     *
     * @param id
     * @return Avatar preview / Превью аватара
     * @see sky.pro.Animals.service.PetAvatarService#findAvatar(Long)
     */
    @GetMapping(path = "{id}/preview")
    public ResponseEntity<byte[]> downloadAvatarPreview(@PathVariable Long id) {
        PetAvatar petAvatar = petAvatarService.findAvatar(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(petAvatar.getMediaType()));
        headers.setContentLength(petAvatar.getData().length);
        return ResponseEntity.ok().headers(headers).body(petAvatar.getData());
    }

    /**
     * API for loading avatar of pet with this id. <br>
     * Used service method {@link sky.pro.Animals.service.PetAvatarService#uploadAvatar(Long, MultipartFile)}. <br>
     * <hr>
     * API для загрузки автара питомца с данным id. <br>
     * Использован метод сервиса {@link sky.pro.Animals.service.PetAvatarService#uploadAvatar(Long, MultipartFile)}. <br>
     * <hr>
     *
     * @param id
     * @param avatar
     * @return ResponseEntity status of upload / ResponseEntity статус загрузки
     * @throws IOException
     * @see sky.pro.Animals.service.PetAvatarService#uploadAvatar(Long, MultipartFile)
     */
    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() >= 1024 * 500) {
            return ResponseEntity.status(400).body("File is too big!");
        }
        petAvatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }
}

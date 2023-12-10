package sky.pro.Animals.service;

import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sky.pro.Animals.entity.Pet;
import sky.pro.Animals.entity.PetAvatar;
import sky.pro.Animals.repository.PetAvatarRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Service for working with pets avatars
 * <p>
 * <hr>
 * <p>
 * Сервис для работы с аватарками питомцев
 */
@Service
@Log4j
@Transactional
public class PetAvatarServiceImpl implements PetAvatarService {
    Logger log = LoggerFactory.getLogger(PetAvatarServiceImpl.class);
    @Value("${pet.avatar.dir.path}")
    private String petAvatarsDir;
    private final PetServiceImpl petService;
    private final PetAvatarRepository petAvatarRepository;

    public PetAvatarServiceImpl(PetServiceImpl petService, PetAvatarRepository petAvatarRepository) {
        this.petService = petService;
        this.petAvatarRepository = petAvatarRepository;
    }

    /**
     * Method for getting pet avatar by id. <br>
     * Used repository method {@link PetAvatarRepository#findById(Object)}. <br>
     * <hr>
     * Метод для получения аватара питомца по id. <br>
     * Использован метод репозитория {@link PetAvatarRepository#findById(Object)}. <br>
     * <hr>
     *
     * @param id
     * @return Pet avatar / Аватар питомца
     * @see PetAvatarRepository#findById(Object)
     */
    @Override
    @Cacheable("avatar")
    public PetAvatar findAvatar(Long id) {
        log.info("Searching avatar by id " + id);
        PetAvatar answer = petAvatarRepository.findById(id).get();
        log.debug("Getting answer " + answer);
        return answer;
    }

    /**
     * Method for saving pet avatar in DB. <br>
     * Used repository method {@link PetAvatarRepository#save(Object)}. <br>
     * <hr>
     * Метод для сохранения аватара питомца в БД. <br>
     * Использован метод репозитория {@link PetAvatarRepository#save(Object)}. <br>
     * <hr>
     *
     * @param id
     * @param file
     * @throws IOException
     */
    @Override
    @CachePut(value = "avatar", key = "#avatar.id")
    public void uploadAvatar(Long id, MultipartFile file) throws IOException {
        log.info("Trying to upload avatar for student with id " + id);
        Pet pet = petService.getById(id);
        Path filePath = Path.of(petAvatarsDir, id + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        PetAvatar petAvatar = getPetAvatar(id);
        petAvatar.setPet(pet);
        petAvatar.setFilePath(filePath.toString());
        petAvatar.setFileSize(file.getSize());
        petAvatar.setMediaType(file.getContentType());
        petAvatar.setData(generateImagePreview(filePath));
        petAvatarRepository.save(petAvatar);
    }

    /**
     * Method for getting image extension. <br>
     * <hr>
     * Метод для получения расширения изображения. <br>
     * <hr>
     *
     * @param filename
     * @return Image extension / Расширение изображения
     */
    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * Method for getting pet avatar by pet id. <br>
     * Used repository method {@link PetAvatarRepository#findByPetId(Long)}. <br>
     * <hr>
     * Метод для получения аватара питомца по его собственному id. <br>
     * Использован метод репозитория {@link PetAvatarRepository#findByPetId(Long)}. <br>
     * <hr>
     *
     * @param id
     * @return Pet avatar / Аватар питомца
     * @see PetAvatarRepository#findByPetId(Long)
     */
    public PetAvatar getPetAvatar(Long id) {
        return petAvatarRepository.findByPetId(id).orElse(new PetAvatar());
    }

    /**
     * Method for generation pet avatar preview. <br>
     * <hr>
     * Метод для генерации превью аватара питомца. <br>
     * <hr>
     *
     * @param filePath
     * @return Pet avatar in byte[] / Аватар питомца в виде byte[]
     * @throws IOException
     */
    public byte[] generateImagePreview(Path filePath) throws IOException {
        try (
                InputStream is = Files.newInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            BufferedImage image = ImageIO.read(bis);
            int height = 0;
            int width = 0;
            BufferedImage preview = null;
            if (image.getHeight() > image.getWidth()) {
                height = image.getHeight() / (image.getWidth() / 100);
                preview = new BufferedImage(100, height, image.getType());
            } else {
                width = image.getWidth() / (image.getHeight() / 100);
                preview = new BufferedImage(width, 100, image.getType());
            }
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();
            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }
}

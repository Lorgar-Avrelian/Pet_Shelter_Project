package sky.pro.Animals.service;

import sky.pro.Animals.entity.Volunteer;

import java.sql.Date;
import java.util.Collection;

public interface VolunteerService {
    Collection<Volunteer> getAll();

    Volunteer getById(Long id);

    Volunteer save(Volunteer volunteer);

    Volunteer save(Long id,
                   String fio,
                   String address,
                   Date birthday,
                   String passport,
                   Long chatId,
                   String workPosition);

    Volunteer delete(Long id);
}

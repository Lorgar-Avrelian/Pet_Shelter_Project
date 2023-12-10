package sky.pro.Animals.service;

import sky.pro.Animals.entity.Info;

import java.util.Collection;

public interface InfoService {
    void checkInfo();

    String editInfo(Info info);

    Collection<Info> getAllInfo();

    String getInfoTextById(Long id);

    String getInfoKeyById(Long id);

    Info getById(Long id);
}

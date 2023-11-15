package sky.pro.Animals.service;

import org.springframework.stereotype.Service;
import sky.pro.Animals.entity.Volunteer;
import sky.pro.Animals.repository.VolunteerRepository;

import java.util.Collection;

@Service
public class VolunteerServiceImpl implements VolunteerService {
    private final VolunteerRepository volunteerRepository;

    public VolunteerServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Override
    public Collection<Volunteer> getAll() {
        return volunteerRepository.findAll();
    }

    @Override
    public Volunteer getById(Long id) {
        return volunteerRepository.findById(id).get();
    }

    @Override
    public Volunteer save(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    @Override
    public Volunteer delete(Long id) {
        Volunteer volunteer = volunteerRepository.getById(id);
        volunteerRepository.delete(volunteer);
        return volunteer;
    }
}

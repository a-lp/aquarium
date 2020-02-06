package fr.upem.devops.service;

import fr.upem.devops.model.Fish;
import fr.upem.devops.repository.FishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FishService {
    @Autowired
    private FishRepository fishRepository;

    public Iterable<Fish> getAll() {

        return fishRepository.findAll();
    }

    public Fish save(Fish fish) {
        return fishRepository.save(fish);
    }

    public void update(Fish fish) {
        fishRepository.save(fish);
    }

    public Fish getById(Long id) {
        return fishRepository.findById(id).get();
    }

    public Fish remove(Fish fish) {
        fishRepository.delete(fish);
        return fish;
    }
}

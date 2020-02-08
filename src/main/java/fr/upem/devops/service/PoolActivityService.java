package fr.upem.devops.service;

import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.repository.PoolActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PoolActivityService {
    @Autowired
    private PoolActivityRepository poolActivityRepository;

    public Iterable<PoolActivity> getAll() {

        return poolActivityRepository.findAll();
    }

    public PoolActivity save(PoolActivity poolActivity) {
        return poolActivityRepository.save(poolActivity);
    }


    public void update(PoolActivity poolActivity) {
        poolActivityRepository.save(poolActivity);
    }

    public PoolActivity getById(Long id) {
        return poolActivityRepository.findById(id).get();
    }

    public PoolActivity remove(PoolActivity poolActivity) {
        poolActivityRepository.delete(poolActivity);
        return poolActivity;
    }
}

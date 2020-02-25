package fr.upem.devops.service;

import fr.upem.devops.model.Pool;
import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.model.Schedule;
import fr.upem.devops.model.Staff;
import fr.upem.devops.repository.PoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;

@Service
public class PoolService {
    @Autowired
    private PoolRepository poolRepository;

    public Iterable<Pool> getAll() {
        return poolRepository.findAll();
    }

    public Pool getById(Long id) {
        return poolRepository.findById(id).orElse(null);
    }

    public Pool save(Pool pool) {
        return poolRepository.save(pool);
    }

    public void update(Pool pool) {
        poolRepository.save(pool);
    }

    public Pool remove(Pool pool) {
        poolRepository.delete(pool);
        return pool;
    }
}

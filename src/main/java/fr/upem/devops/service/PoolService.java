package fr.upem.devops.service;

import fr.upem.devops.model.Pool;
import fr.upem.devops.repository.PoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Pool save(Pool specie) {
        return poolRepository.save(specie);
    }

    public void update(Pool specie) {
        poolRepository.save(specie);
    }

    public Pool remove(Pool specie) {
        poolRepository.delete(specie);
        return specie;
    }
}

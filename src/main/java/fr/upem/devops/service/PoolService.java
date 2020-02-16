package fr.upem.devops.service;

import fr.upem.devops.model.Fish;
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

    public Pool save(Pool pool) {
        return poolRepository.save(pool);
    }

    public void update(Pool pool) {
        poolRepository.save(pool);
    }

    public Pool remove(Pool pool) {
        for (Fish f : pool.getFishes()) {
            f.setPool(null);
        }
        if (pool.getResponsible() != null)
            pool.getResponsible().removePoolResponsability(pool);
        poolRepository.delete(pool);
        return pool;
    }
}

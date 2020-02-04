package fr.upem.devops.repository;

import fr.upem.devops.model.Pool;
import org.springframework.data.repository.CrudRepository;

public interface PoolRepository extends CrudRepository<Pool, Long> {
}

package fr.upem.devops.repository;

import fr.upem.devops.model.Fish;
import org.springframework.data.repository.CrudRepository;

public interface FishRepository extends CrudRepository<Fish, Long> {
}

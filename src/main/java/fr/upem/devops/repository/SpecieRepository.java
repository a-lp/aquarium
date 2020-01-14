package fr.upem.devops.repository;

import fr.upem.devops.model.Specie;
import org.springframework.data.repository.CrudRepository;

public interface SpecieRepository extends CrudRepository<Specie, Long> {
}

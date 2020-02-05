package fr.upem.devops.repository;

import fr.upem.devops.model.Sector;
import org.springframework.data.repository.CrudRepository;

public interface SectorRepository extends CrudRepository<Sector, Long> {
}

package fr.upem.devops.repository;

import fr.upem.devops.model.Pool;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PoolRepository extends CrudRepository<Pool, Long> {
    @Query("SELECT p FROM Pool p WHERE p.responsible.id = :id")
    Iterable<Pool> findPoolsByResponsible(@Param("id") Long id);
}

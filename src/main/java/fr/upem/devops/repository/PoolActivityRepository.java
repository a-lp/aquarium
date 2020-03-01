package fr.upem.devops.repository;

import fr.upem.devops.model.Pool;
import fr.upem.devops.model.PoolActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PoolActivityRepository extends CrudRepository<PoolActivity, Long> {
    @Query("SELECT p FROM PoolActivity p WHERE p.openToPublic = true ")
    Iterable<PoolActivity> findPoolsOpenToPublic();
}

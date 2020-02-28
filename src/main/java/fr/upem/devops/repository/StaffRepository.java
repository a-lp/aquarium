package fr.upem.devops.repository;

import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Staff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StaffRepository extends CrudRepository<Staff, Long> {
    @Query("SELECT p FROM Staff p WHERE p.role = :role")
    Iterable<Staff> findByRole(@Param("role") Staff.StaffRole role);
}

package fr.upem.devops.repository;

import fr.upem.devops.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByToken(String token);
}

package fr.upem.devops.service;


import fr.upem.devops.model.User;
import fr.upem.devops.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getByToken(String token) {
        return userRepository.findByToken(token);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

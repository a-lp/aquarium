package fr.upem.devops.service;

import fr.upem.devops.model.Staff;
import fr.upem.devops.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserRegistrationService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthenticationService authenticationService;

    public String register(String username, String password, Staff profile) throws IllegalArgumentException {
        User user = new User();
        user.setUsername(username);
        user.setPassword(user.hashPassword(password));
        user.setProfile(profile);
        profile.setCredentials(user);
        userService.save(user);

        return authenticationService.login(username, password);
    }
}

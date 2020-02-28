package fr.upem.devops.service;

import fr.upem.devops.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class JWTAuthenticationService implements UserAuthenticationService {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private StaffService staffService;

    @Override
    public String login(String username, String password) throws BadCredentialsException {

        return userService
                .getByUsername(username)
                .filter(user -> user.checkPassword(password))
                .filter(user -> user.getProfile() != null)
                .map(user -> jwtService.create(username, user.getProfile()))
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password."));
    }

    @Override
    public User authenticateByToken(String token) {
        try {
            Map<String, Object> tokenParameters = jwtService.verify(token);
            Object username = tokenParameters.get("username");
            Object profile = tokenParameters.get("id");
            if (profile == null || staffService.getById(Long.parseLong(profile.toString())) == null) {
                throw new BadCredentialsException("No profile associated!");
            }
            return Optional.ofNullable(username)
                    .flatMap(name -> userService.getByUsername(String.valueOf(name)))
                    .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found."));
        } catch (TokenVerificationException e) {
            throw new BadCredentialsException("Invalid JWT token.", e);
        }
    }

    @Override
    public void logout(String username) {
    }
}

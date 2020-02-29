package fr.upem.devops.controller;

import fr.upem.devops.model.Staff;
import fr.upem.devops.model.User;
import fr.upem.devops.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static fr.upem.devops.WebApplication.firstGenerationToken;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
public class PublicEndpointsController {
    @Autowired
    private UserRegistrationService registrationService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthenticationService authenticationService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private JWTService jwtService;


    @PostMapping("/register")
    public Object register(@RequestBody User user, @RequestParam(value = "token", required = false) String token, @RequestHeader(value = "Authorization", required = false) String staffToken) {
        // Missing staff token
        if (staffToken != null) {
            try {
                jwtService.verify(staffToken.replace("Bearer", "").trim());
            } catch (TokenVerificationException e) {
                return ResponseEntity.status(UNAUTHORIZED).body("Ask the administrator for the correct token!");
            }
        }
        // Missing firstGenerationToken
        else {
            if (firstGenerationToken == null) {
                return ResponseEntity.status(UNAUTHORIZED).body("Ask the administrator for the registration of a new staff component!");
            }
            int size = 0;
            for (Staff s : staffService.getByRole(Staff.StaffRole.ADMIN)) {
                size++;
            }
            if (!firstGenerationToken.equals(token) || size > 0) {
                return ResponseEntity.status(UNAUTHORIZED).body("Ask the administrator for the registration of a new staff component!");
            } else {
                firstGenerationToken = null;
            }
        }
        try {
            userService.getByUsername(user.getUsername())
                    .ifPresent(u -> {
                        throw new IllegalArgumentException("Username already in use.");
                    });
            Staff profile = staffService.save(user.getProfile());
            return registrationService
                    .register(user.getUsername(), user.getPassword(), profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Object login(@RequestBody HashMap<String, String> parameters) {
        String username = null, password = null;
        if (parameters.containsKey("username")) username = parameters.get("username");
        else return ResponseEntity.badRequest().body("Required String parameter 'username' is not present");
        if (parameters.containsKey("password")) password = parameters.get("password");
        else return ResponseEntity.badRequest().body("Required String parameter 'password' is not present");
        try {
            return authenticationService
                    .login(username, password);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(e.getMessage());
        }
    }
}

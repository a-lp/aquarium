package fr.upem.devops.controller;

import fr.upem.devops.model.Staff;
import fr.upem.devops.model.User;
import fr.upem.devops.service.StaffService;
import fr.upem.devops.service.UserAuthenticationService;
import fr.upem.devops.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
public class PublicEndpointsController {
    @Autowired
    private UserRegistrationService registrationService;
    @Autowired
    private UserAuthenticationService authenticationService;
    @Autowired
    private StaffService staffService;

    //    @PostMapping("/register")
//    public Object register(@RequestBody HashMap<String, String> parameters) {
//        String username = null, password = null;
//        if (parameters.containsKey("username")) username = parameters.get("username");
//        else return ResponseEntity.badRequest().body("Required String parameter 'username' is not present");
//        if (parameters.containsKey("password")) password = parameters.get("password");
//        else return ResponseEntity.badRequest().body("Required String parameter 'password' is not present");
//        try {
//            return registrationService
//                    .register(username, password);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
    @PostMapping("/register")
    public Object register(@RequestBody User user) {
        Staff profile = staffService.save(user.getProfile());
        try {
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

package fr.upem.devops.controller;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import fr.upem.devops.WebApplication;
import fr.upem.devops.model.Staff;
import fr.upem.devops.model.User;
import fr.upem.devops.service.JWTAuthenticationService;
import fr.upem.devops.service.JWTService;
import fr.upem.devops.service.StaffService;
import fr.upem.devops.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PublicEndpointsControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private StaffService staffService;
    @MockBean
    private JWTAuthenticationService authenticationService;
    @MockBean
    private JWTService jwtService;
    @MockBean
    private WebApplication webApplication;

    private HttpHeaders headers = new HttpHeaders();
    private String username;
    private String password;
    private String token;
    private Staff staff;
    private Staff staffNew;
    private User user;
    private User userNew;
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void init() {
        username = "usernameTest";
        password = "passwordTest";
        staff = new Staff();
        staff.setCredentials(this.user);
        staff.setRole(Staff.StaffRole.ADMIN);
        staffNew = new Staff();
        staffNew.setId(1L);
        staffNew.setCredentials(this.user);
        staffNew.setRole(Staff.StaffRole.ADMIN);
        user = new User();
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setProfile(staff);
        userNew = new User();
        userNew.setUsername(this.username);
        userNew.setPassword(this.password);
        userNew.setId(1L);
        userNew.setProfile(staffNew);
        token = jwtService.create(username, staffNew);
        headers.add("Authorization", "Bearer " + this.token);
        Map<String, Object> result = jwtService.verify(token);
        Mockito.when(staffService.save(staff)).thenReturn(staffNew);
        Mockito.when(userService.save(user)).thenReturn(userNew);
        Mockito.when(userService.getByUsername(user.getUsername())).thenReturn(null);
        Mockito.when(jwtService.create(username, staff)).thenReturn(token);
        Mockito.when(jwtService.verify(token)).thenReturn(result);
    }

    @Test
    public void registerByStaff() {
        HttpEntity<User> httpEntity = new HttpEntity(user, this.headers);
        ResponseEntity<String> request = this.restTemplate.postForEntity("http://localhost:" + port + "/register", httpEntity, String.class);
        assertEquals(token, request.getBody());
    }

    @Test
    public void registerByToken() {
        Mockito.when(staffService.getByRole(Staff.StaffRole.ADMIN)).thenReturn(new ArrayIterator<>(new Staff[0]));
        ResponseEntity<String> request = this.restTemplate.postForEntity("http://localhost:" + port + "/register?token=testingOnly", user, String.class);
        assertEquals(token, request.getBody());
    }

    @Test
    public void registerByTokenUnauthorized() {
        Staff[] staffs = {staff};
        Mockito.when(staffService.getByRole(Staff.StaffRole.ADMIN)).thenReturn(new ArrayIterator<>(staffs));
        ResponseEntity<String> request = this.restTemplate.postForEntity("http://localhost:" + port + "/register?token=testingOnly", user, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, request.getStatusCode());
        assertEquals("Ask the administrator for the registration of a new staff component!", request.getBody());
    }

    @Test
    public void registerByBadTokenUnauthorized() {
        Staff[] staffs = {staff};
        Mockito.when(staffService.getByRole(Staff.StaffRole.ADMIN)).thenReturn(new ArrayIterator<>(staffs));
        ResponseEntity<String> request = this.restTemplate.postForEntity("http://localhost:" + port + "/register?token=badToken", user, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, request.getStatusCode());
        assertEquals("Ask the administrator for the registration of a new staff component!", request.getBody());
    }

    @Test
    public void registerDuplicate() {
        HttpEntity<User> httpEntity = new HttpEntity(user, this.headers);
        Mockito.when(userService.getByUsername(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity<String> request = this.restTemplate.postForEntity("http://localhost:" + port + "/register", httpEntity, String.class);
        assertEquals("Username already in use.", request.getBody());
    }

    @Test
    public void login() {
        Mockito.when(userService.getByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(authenticationService.login(username, password)).thenReturn(token);
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);
        ResponseEntity<String> request = this.restTemplate.postForEntity("http://localhost:" + port + "/login", parameters, String.class);
        assertEquals(token, request.getBody());
    }

    @Test
    public void loginMissingCredentials() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        ResponseEntity<String> request = this.restTemplate.postForEntity("http://localhost:" + port + "/login", parameters, String.class);
        assertEquals("Required String parameter 'password' is not present", request.getBody());
        parameters.remove("username");
        parameters.put("password", password);
        request = this.restTemplate.postForEntity("http://localhost:" + port + "/login", parameters, String.class);
        assertEquals("Required String parameter 'username' is not present", request.getBody());
    }

    @Test
    public void loginBadCredentials() {
        Mockito.when(userService.getByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(authenticationService.login(username, "not correct password")).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid username or password."));
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", "not correct password");
        ResponseEntity<String> request = this.restTemplate.postForEntity("http://localhost:" + port + "/login", parameters, String.class);
        assertEquals(HttpStatus.NOT_FOUND, request.getStatusCode());
    }
}

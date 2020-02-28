package fr.upem.devops.controller;

import fr.upem.devops.model.*;
import fr.upem.devops.service.JWTAuthenticationService;
import fr.upem.devops.service.JWTService;
import fr.upem.devops.service.StaffService;
import fr.upem.devops.service.TokenVerificationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StaffControllerTest {
    private User user;
    private String token = "tokenTest";
    private Staff profile;
    private HashMap<String, Object> tokenParameters = new HashMap<>();
    private HttpHeaders headers = new HttpHeaders();
    @MockBean
    private JWTService jwtService;
    @MockBean
    private JWTAuthenticationService authenticationService;
    @MockBean
    private StaffService staffService;
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Staff> staffList = new ArrayList<>();

    @Before
    public void init() {
        Staff s1 = new Staff(1L, "Nome1", "Cognome1", "Address1", new Date(), "SocSec1", Staff.StaffRole.ADMIN);
        Staff s2 = new Staff(2L, "Nome2", "Cognome2", "Address2", new Date(), "SocSec2", Staff.StaffRole.MANAGER);
        Staff s3 = new Staff(3L, "Nome3", "Cognome3", "Address3", new Date(), "SocSec3", Staff.StaffRole.WORKER);
        Pool p1 = new Pool(1L, 10L, 10.5, Pool.WaterCondition.CLEAN, new HashSet<>());
        Pool p2 = new Pool(2L, 20L, 20.5, Pool.WaterCondition.CLEAN, new HashSet<>());
        Pool p3 = new Pool(3L, 30L, 30.5, Pool.WaterCondition.DIRTY, new HashSet<>());
        Sector sec1 = new Sector(1L, "Sector1", "Location1");
        Sector sec2 = new Sector(2L, "Sector2", "Location2");
        Sector sec3 = new Sector(3L, "Sector3", "Location3");

        s1.assignPool(p1);
        s2.assignPool(p2);
        s3.assignPool(p3);
        s1.assignSector(sec1);
        s2.assignSector(sec2);
        s3.assignSector(sec3);

        staffList.add(s1);
        staffList.add(s2);
        staffList.add(s3);
        Mockito.when(staffService.getAll()).thenReturn(staffList);
        Mockito.when(staffService.getById(1l)).thenReturn(s1);
        Mockito.when(staffService.getById(2l)).thenReturn(s2);
        Mockito.when(staffService.getById(3l)).thenReturn(s3);
        /* Spring Security */
        this.user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        this.profile = new Staff();
        this.profile.setCredentials(this.user);
        this.profile.setId(100L);
        this.profile.setRole(Staff.StaffRole.ADMIN);
        this.user.setProfile(this.profile);
        this.tokenParameters.put("iat", new Date().getTime());
        this.tokenParameters.put("exp", Date.from(Instant.now().plusSeconds(60 * 5000)).getTime());
        this.tokenParameters.put("username", this.user.getUsername());
        this.tokenParameters.put("id", this.user.getProfile().getId());
        this.tokenParameters.put("role", this.user.getProfile().getRole().name());
        this.headers.add("Authorization", "Bearer " + this.token);
        Mockito.when(jwtService.create(this.user.getUsername(), this.user.getProfile())).thenReturn(this.token);
        Mockito.when(jwtService.verify(this.token)).thenReturn(this.tokenParameters);
        Mockito.when(authenticationService.authenticateByToken("tokenTest")).thenReturn(this.user);
    }

    @Test
    public void getAll() {
        HttpEntity<Staff> httpEntity = new HttpEntity<>(null, this.headers);
        List lista = this.restTemplate.exchange("http://localhost:" + port + "/api/staff", HttpMethod.GET, httpEntity, List.class).getBody();
        assertEquals(3, lista.size());
    }

    @Test
    public void getById() {
        HttpEntity<Staff> httpEntity = new HttpEntity<>(null, this.headers);
        List<HashMap> lista = this.restTemplate.exchange("http://localhost:" + port + "/api/staff", HttpMethod.GET, httpEntity, List.class).getBody();
        HashMap<String, Object> output = this.restTemplate.exchange("http://localhost:" + port + "/api/staff/1", HttpMethod.GET, httpEntity, HashMap.class).getBody();
        assertEquals(lista.get(0).get("id").toString(), output.get("id").toString());
        assertEquals(lista.get(0).get("name").toString(), output.get("name"));
        assertEquals(lista.get(0).get("surname"), output.get("surname"));
//        assertEquals(lista.get(0).get("birthday"), output.getBirthday());
        assertEquals(lista.get(0).get("socialSecurity"), output.get("socialSecurity"));
        assertEquals(lista.get(0).get("role"), output.get("role"));
        assertEquals(((List) lista.get(0).get("poolsResponsabilities")).size(), ((List) output.get("poolsResponsabilities")).size());
        assertEquals(((List) lista.get(0).get("sectors")).size(), ((List) output.get("sectors")).size());
    }

    @Test
    public void addStaff() {
        Date date = new Date();
        Staff staff = new Staff(4L, "Nome4", "Cognome4", "Address4", date, "SocSec4", Staff.StaffRole.WORKER);
        Mockito.when(staffService.save(staff)).thenReturn(new Staff(4L, "Nome4", "Cognome4", "Address4", date, "SocSec4", Staff.StaffRole.WORKER));
        HttpEntity<Staff> httpEntity = new HttpEntity<>(staff, this.headers);
        Staff request = this.restTemplate.postForObject("http://localhost:" + port + "/api/staff", httpEntity,
                Staff.class);
        assertEquals(request.getId(), staff.getId());
        assertEquals(request.getName(), staff.getName());
        assertEquals(request.getSurname(), staff.getSurname());
        assertEquals(request.getSocialSecurity(), staff.getSocialSecurity());
        assertEquals(request.getRole(), staff.getRole());
        assertEquals(request.getPoolsResponsabilities().size(), staff.getPoolsResponsabilities().size());
        assertEquals(request.getSectors().size(), staff.getSectors().size());
    }

    @Test
    public void updateStaff() {
        Staff staff = this.staffList.get(0);
        Mockito.when(staffService.save(staff)).thenReturn(staff);
        HashMap<String, String> parameters = new HashMap<>();
        staff.setSurname("New Surname");
        staff.setSocialSecurity("New SeocialSec");
        parameters.put("surname", staff.getSurname());
        parameters.put("socialSecurity", staff.getSocialSecurity());
        HttpEntity<HashMap> httpEntity = new HttpEntity<>(parameters, this.headers);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/api/staff/1", HttpMethod.PUT, httpEntity, HashMap.class).getBody();
        assertEquals(staff.getId().toString(), request.get("id").toString());
        assertEquals(staff.getSurname(), request.get("surname"));
        assertEquals(staff.getSocialSecurity(), request.get("socialSecurity"));
    }

    @Test
    public void deleteStaff() {
        Staff staff = this.staffList.get(0);
        Mockito.when(staffService.remove(staff)).thenReturn(staff);
        HttpEntity<Staff> httpEntity = new HttpEntity<>(null, this.headers);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/api/staff/1", HttpMethod.DELETE, httpEntity, HashMap.class).getBody();
        assertEquals(staff.getId().toString(), request.get("id").toString());
    }


    @Test
    public void getByIdNotFound() {
        Mockito.when(staffService.getById(10L)).thenReturn(null);
        HttpEntity<Staff> httpEntity = new HttpEntity<>(null, this.headers);
        ResponseEntity<Staff> response = this.restTemplate.exchange("http://localhost:" + port + "/api/staff/10", HttpMethod.GET, httpEntity, Staff.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByIdBadRequest() {
        HttpEntity<Staff> httpEntity = new HttpEntity<>(null, this.headers);
        ResponseEntity<Staff> response = this.restTemplate.exchange("http://localhost:" + port + "/api/staff/asdf", HttpMethod.GET, httpEntity, Staff.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateBadRequest() {
        Mockito.when(staffService.getById(1L)).thenReturn(this.staffList.get(0));
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("birthday", "ww");
        HttpEntity<HashMap> updated = new HttpEntity<>(parameters, this.headers);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/api/staff/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("'" + parameters.get("birthday") + "' is not a valid value to be converted in date. Insert a Long representing the time in milliseconds", response.getBody().get("message"));
        parameters.remove("birthday");
        parameters.put("role", "zxcv");
        updated = new HttpEntity<>(parameters, this.headers);
        response = this.restTemplate.exchange("http://localhost:" + port + "/api/staff/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("'" + parameters.get("role") + "' is not a valid value to be converted in StaffRole.", response.getBody().get("message"));
    }

    @Test
    public void deleteStaffWithActivitiesBadRequest() {
        Staff staff = new Staff();
        staff.setId(10L);
        PoolActivity activity = new PoolActivity();
        activity.assignStaff(staff);
        staff.assignActivity(activity);
        Mockito.when(staffService.getById(10L)).thenReturn(staff);
        HttpEntity<Staff> httpEntity = new HttpEntity<>(null, this.headers);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/api/staff/10", HttpMethod.DELETE, httpEntity, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Activities must have at least one responsible! Removing this staff, will also remove the only one responsible", response.getBody().get("message"));
    }

    @Test
    public void deleteStaffWithSectorsBadRequest() {
        Staff staff = new Staff();
        staff.setId(10L);
        Sector sector = new Sector(1L, "Sector1", "Location1");
        sector.assignStaff(staff);
        staff.assignSector(sector);
        Mockito.when(staffService.getById(10L)).thenReturn(staff);
        staff.setActivities(new HashSet<>());
        HttpEntity<Staff> httpEntity = new HttpEntity<>(null, this.headers);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/api/staff/10", HttpMethod.DELETE, httpEntity, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sectors must have at least one responsible! Removing this staff, will also remove the only one responsible", response.getBody().get("message"));
    }

    @Test
    public void invalidTokenGet() {
        Mockito.when(authenticationService.authenticateByToken("not.a.good.token")).thenThrow(BadCredentialsException.class);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer not.a.good.token");
        HttpEntity<Staff> httpEntity = new HttpEntity<>(null, corruptedHeader);
        ResponseEntity<String> response = this.restTemplate.exchange("http://localhost:" + port + "/api/staff", HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void invalidTokenPost() {
        Mockito.when(authenticationService.authenticateByToken("not.a.good.token")).thenThrow(BadCredentialsException.class);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer not.a.good.token");
        HttpEntity<Staff> httpEntity = new HttpEntity<>(new Staff(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/staff", httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    @Test
    public void invalidTokenPut() {
        Mockito.when(authenticationService.authenticateByToken("not.a.good.token")).thenThrow(BadCredentialsException.class);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer not.a.good.token");
        HttpEntity<Staff> httpEntity = new HttpEntity<>(new Staff(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate
                .exchange("http://localhost:" + port + "/api/staff/3", HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    @Test
    public void invalidTokenDelete() {
        Mockito.when(authenticationService.authenticateByToken("not.a.good.token")).thenThrow(BadCredentialsException.class);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer not.a.good.token");
        HttpEntity<Staff> httpEntity = new HttpEntity<>(new Staff(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate
                .exchange("http://localhost:" + port + "/api/staff/3", HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}

package fr.upem.devops.controller;

import fr.upem.devops.model.*;
import fr.upem.devops.service.*;
import org.assertj.core.util.Sets;
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
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PoolActivityControllerTest {
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
    private PoolActivityService activityService;
    @MockBean
    private ScheduleService scheduleService;
    @MockBean
    private StaffService staffService;

    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<PoolActivity> activities = new ArrayList<>();

    @Before
    public void init() {
        PoolActivity pa1 = new PoolActivity(1L, LocalTime.of(1, 0), LocalTime.of(2, 0), true, new HashSet<>(), null);
        PoolActivity pa2 = new PoolActivity(2L, LocalTime.of(2, 0), LocalTime.of(4, 0), true, new HashSet<>(), null);
        PoolActivity pa3 = new PoolActivity(3L, LocalTime.of(3, 0), LocalTime.of(6, 0), true, new HashSet<>(), null);

        activities.add(pa1);
        activities.add(pa2);
        activities.add(pa3);

        Mockito.when(activityService.getAll()).thenReturn(activities);
        Mockito.when(activityService.getById(1L)).thenReturn(pa1);
        Mockito.when(activityService.getById(2L)).thenReturn(pa2);
        Mockito.when(activityService.getById(3L)).thenReturn(pa3);
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
        this.headers.add("Content-Type", "application/json");
        Mockito.when(jwtService.create(this.user.getUsername(), this.user.getProfile())).thenReturn(this.token);
        Mockito.when(jwtService.verify(this.token)).thenReturn(this.tokenParameters);
        Mockito.when(authenticationService.authenticateByToken("tokenTest")).thenReturn(this.user);
    }

    @Test
    public void getAll() {
        List lista = this.restTemplate.getForObject("http://localhost:" + port + "/api/activities", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getById() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/api/activities", List.class);
        PoolActivity output = this.restTemplate.getForObject("http://localhost:" + port + "/api/activities/1", PoolActivity.class);
        assertEquals(lista.get(0).get("id").toString(), output.getId().toString());
        assertEquals(lista.get(0).get("description"), output.getDescription());
        assertEquals(lista.get(0).get("openToPublic"), output.getOpenToPublic());
        assertEquals(((List) (lista.get(0).get("staffList"))).size(), output.getStaffList().size());
        assertEquals(lista.get(0).get("schedule"), output.getSchedule());
    }

    @Test
    public void addPoolActivity() {
        Schedule schedule = new Schedule(1L, new Date(), new Date(), new HashSet<>());
        Mockito.when(scheduleService.getById(1L)).thenReturn(schedule);
        Staff s1 = new Staff(1L, "Nome1", "Cognome1", "Address1", new Date(), "SocSec1", Staff.StaffRole.ADMIN);
        Staff s2 = new Staff(2L, "Nome2", "Cognome2", "Address2", new Date(), "SocSec2", Staff.StaffRole.MANAGER);
        Staff s3 = new Staff(3L, "Nome3", "Cognome3", "Address3", new Date(), "SocSec3", Staff.StaffRole.WORKER);
        Mockito.when(staffService.getById(1L)).thenReturn(s1);
        Mockito.when(staffService.getById(2L)).thenReturn(s2);
        Mockito.when(staffService.getById(3L)).thenReturn(s3);
        PoolActivity pa = new PoolActivity(LocalTime.of(4, 0), LocalTime.of(8, 0), true, new HashSet<>(), null);
        PoolActivity pa_new = new PoolActivity(4L, LocalTime.of(4, 0), LocalTime.of(8, 0), true, Sets.newHashSet(Arrays.asList(s1, s2, s3)), schedule);
        schedule.assignActivity(pa_new);
        Mockito.when(activityService.save(pa)).thenReturn(pa_new);
        HttpEntity<PoolActivity> httpEntity = new HttpEntity(pa, this.headers);
        HashMap<String, Object> request = this.restTemplate.postForObject("http://localhost:" + port + "/api/schedule/1/activities/staff/1,2,3", httpEntity,
                HashMap.class);
        assertEquals(pa_new.getId().toString(), request.get("id").toString());
        assertEquals(pa_new.getDescription(), request.get("description"));
        assertEquals(pa_new.getOpenToPublic(), request.get("openToPublic"));
        assertEquals(pa_new.getSchedule().getId().toString(), request.get("schedule").toString());
        assertEquals(pa_new.getEndActivity(), LocalTime.parse(request.get("endActivity").toString()));
        assertEquals(pa_new.getStartActivity(), LocalTime.parse(request.get("startActivity").toString()));
        assertEquals(pa_new.getStaffList().size(), ((List) request.get("staffList")).size());
    }

    @Test
    public void updatePoolActivity() {
        PoolActivity poolActivity = this.activities.get(0);
        Mockito.when(activityService.save(poolActivity)).thenReturn(poolActivity);
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("description", "false");
        HttpEntity<HashMap> updated = new HttpEntity<>(parameters, this.headers);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/api/activities/1", HttpMethod.PUT, updated, HashMap.class).getBody();
        assertEquals("1", request.get("id").toString());
        assertEquals("false", request.get("description"));

    }

    @Test
    public void deleteActivity() {
        PoolActivity poolActivity = this.activities.get(0);
        Mockito.when(activityService.remove(poolActivity)).thenReturn(poolActivity);
        HttpEntity<PoolActivity> httpEntity = new HttpEntity(null, this.headers);
        PoolActivity response = this.restTemplate.exchange("http://localhost:" + port + "/api/activities/1", HttpMethod.DELETE, httpEntity, PoolActivity.class).getBody();
        assertEquals(Long.valueOf(1L), response.getId());
        assertEquals(poolActivity, response);
    }

    /* HTTP EXCEPTIONS */
    @Test
    public void addPoolActivityScheduleNotFound() {
        Mockito.when(scheduleService.getById(1L)).thenReturn(null);
        HttpEntity<PoolActivity> httpEntity = new HttpEntity(new PoolActivity(), this.headers);
        ResponseEntity<PoolActivity> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/schedule/1/activities/staff/1,2,3", httpEntity,
                PoolActivity.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addPoolActivityResponsibleNotFound() {
        Schedule schedule = new Schedule(1L, new Date(), new Date(), new HashSet<>());
        Mockito.when(scheduleService.getById(1L)).thenReturn(schedule);
        Mockito.when(staffService.getById(1L)).thenReturn(null);
        HttpEntity<PoolActivity> httpEntity = new HttpEntity(new PoolActivity(), this.headers);
        ResponseEntity<PoolActivity> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/schedule/1/activities/staff/1,2,3", httpEntity,
                PoolActivity.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addPoolActivityResponsibleBadRequest() {
        Schedule schedule = new Schedule(1L, new Date(), new Date(), new HashSet<>());
        Mockito.when(scheduleService.getById(1L)).thenReturn(schedule);
        HttpEntity<PoolActivity> httpEntity = new HttpEntity(null, this.headers);
        ResponseEntity<PoolActivity> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/schedule/1/activities/staff/a,3", httpEntity,
                PoolActivity.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void getByIdNotFound() {
        Mockito.when(activityService.getById(10L)).thenReturn(null);
        ResponseEntity<PoolActivity> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/activities/10", PoolActivity.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByIdBadRequest() {
        ResponseEntity<PoolActivity> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/activities/asdf", PoolActivity.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateBadRequest() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("startActivity", "wow");
        HttpEntity<HashMap> updated = new HttpEntity<>(parameters, this.headers);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/api/activities/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error converting startActivity: '" + parameters.get("startActivity") + "' into LocalTime!", response.getBody().get("message"));
        parameters.remove("startActivity");
        parameters.put("staffList", "a,2");
        updated = new HttpEntity<>(parameters, this.headers);
        response = this.restTemplate.exchange("http://localhost:" + port + "/api/activities/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cannot convert staff id: 'a' into Long", response.getBody().get("message"));
        parameters.remove("staffList");
        parameters.put("staffList", "");
        updated = new HttpEntity<>(parameters, this.headers);
        response = this.restTemplate.exchange("http://localhost:" + port + "/api/activities/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Activities must have at least one responsible in charge!", response.getBody().get("message"));
    }

    @Test
    public void invalidTokenPost() {
        Mockito.when(authenticationService.authenticateByToken("not.a.good.token")).thenThrow(BadCredentialsException.class);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer not.a.good.token");
        HttpEntity<PoolActivity> httpEntity = new HttpEntity<>(new PoolActivity(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/schedule/1/activities/staff/1,2,3", httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    @Test
    public void invalidTokenPut() {
        Mockito.when(authenticationService.authenticateByToken("not.a.good.token")).thenThrow(BadCredentialsException.class);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer not.a.good.token");
        HttpEntity<PoolActivity> httpEntity = new HttpEntity<>(new PoolActivity(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate
                .exchange("http://localhost:" + port + "/api/pools/3", HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    @Test
    public void invalidTokenDelete() {
        Mockito.when(authenticationService.authenticateByToken("not.a.good.token")).thenThrow(BadCredentialsException.class);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer not.a.good.token");
        HttpEntity<PoolActivity> httpEntity = new HttpEntity<>(new PoolActivity(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate
                .exchange("http://localhost:" + port + "/api/pools/3", HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}

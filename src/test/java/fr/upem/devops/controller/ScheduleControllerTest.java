package fr.upem.devops.controller;

import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Schedule;
import fr.upem.devops.model.Staff;
import fr.upem.devops.model.User;
import fr.upem.devops.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBeans({@MockBean(ScheduleService.class), @MockBean(PoolActivityService.class)})
public class ScheduleControllerTest {
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
    private ScheduleService scheduleService;
    @MockBean
    private PoolService poolService;
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Schedule> schedules = new ArrayList<>();
    private List<Pool> pools = new ArrayList<>();

    @Before
    public void init() {
        Pool p1 = new Pool(1L, 10L, 10.5, Pool.WaterCondition.CLEAN, new HashSet<>());
        Pool p2 = new Pool(2L, 20L, 20.5, Pool.WaterCondition.CLEAN, new HashSet<>());
        Pool p3 = new Pool(3L, 30L, 30.5, Pool.WaterCondition.DIRTY, new HashSet<>());
        pools.addAll(Arrays.asList(p1, p2, p3));
        for (int i = 0; i < 3; i++) {
            this.schedules.add(new Schedule(Long.parseLong((i + 1) + ""), new Date(), new Date(), new HashSet<>()));
            this.schedules.get(i).setPool(pools.get(i));
            pools.get(i).getSchedules().add(this.schedules.get(i));
        }
        Mockito.when(scheduleService.getAll()).thenReturn(schedules);
        Mockito.when(scheduleService.getById(1L)).thenReturn(this.schedules.get(0));
        Mockito.when(scheduleService.getById(2L)).thenReturn(this.schedules.get(1));
        Mockito.when(scheduleService.getById(3L)).thenReturn(this.schedules.get(2));
        Mockito.when(poolService.getById(1L)).thenReturn(this.pools.get(0));
        Mockito.when(poolService.getById(2L)).thenReturn(this.pools.get(1));
        Mockito.when(poolService.getById(3L)).thenReturn(this.pools.get(2));
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
        List lista = this.restTemplate.getForObject("http://localhost:" + port + "/api/schedules", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getById() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/api/schedules", List.class);
        HashMap<String, Object> output = this.restTemplate.getForObject("http://localhost:" + port + "/api/schedules/1", HashMap.class);
        assertEquals(lista.get(0).get("id").toString(), output.get("id").toString());
        assertEquals(lista.get(0).get("pool").toString(), output.get("pool").toString());
    }

    @Test
    public void addSchedule() {
        Schedule schedule = new Schedule(new Date(), new Date(), new HashSet<>());
        Schedule schedule_new = new Schedule(4L, new Date(), new Date(), new HashSet<>());
        Pool pool = this.pools.get(0);
        schedule_new.setPool(pool);
        pool.getSchedules().add(schedule_new);
        Mockito.when(scheduleService.save(schedule)).thenReturn(schedule_new);
        HttpEntity<Schedule> httpEntity = new HttpEntity<>(schedule, this.headers);
        HashMap<String, Object> request = this.restTemplate.postForObject("http://localhost:" + port + "/api/pools/1/schedules", httpEntity,
                HashMap.class);
        assertEquals(schedule_new.getId().toString(), request.get("id").toString());
        assertEquals(schedule_new.getScheduledActivities().size(), ((List) request.get("scheduledActivities")).size());
        assertEquals(schedule_new.getPool().getId().toString(), request.get("pool").toString());
    }

    @Test
    public void updateSchedule() {
        Schedule schedule = schedules.get(0);
        schedule.setStartPeriod(new Date());
        schedule.setEndPeriod(new Date());
        Mockito.when(scheduleService.save(schedule)).thenReturn(schedule);
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("endPeriod", schedule.getEndPeriod().getTime() + "");
        parameters.put("startPeriod", schedule.getStartPeriod().getTime() + "");
        HttpEntity<HashMap> httpEntity = new HttpEntity<>(parameters, this.headers);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/api/schedules/1", HttpMethod.PUT, httpEntity, HashMap.class).getBody();
        assertEquals(schedule.getId().toString(), request.get("id").toString());
    }

    @Test
    public void deleteSchedule() {
        Schedule schedule = this.schedules.get(0);
        Mockito.when(scheduleService.remove(schedule)).thenReturn(schedule);
        HttpEntity<Schedule> httpEntity = new HttpEntity<>(null, this.headers);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/api/schedules/1", HttpMethod.DELETE, httpEntity, HashMap.class).getBody();
        assertEquals(schedule.getId().toString(), request.get("id").toString());
    }

    /* HTTP EXCEPTIONS */

    @Test
    public void getByIdNotFound() {
        Mockito.when(scheduleService.getById(10L)).thenReturn(null);
        ResponseEntity<Schedule> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/schedules/10", Schedule.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByIdBadRequest() {
        ResponseEntity<Schedule> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/schedules/asdf", Schedule.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void addSchedulePoolNotFound() {
        Mockito.when(poolService.getById(2L)).thenReturn(null);
        HttpEntity<Schedule> httpEntity = new HttpEntity<>(new Schedule(), this.headers);
        ResponseEntity<Schedule> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/pools/2/schedules", httpEntity,
                Schedule.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateBadRequest() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("startPeriod", "wow");
        HttpEntity<HashMap> updated = new HttpEntity<>(parameters, this.headers);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/api/schedules/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error converting startPeriod: '" + parameters.get("startPeriod") + "' into Date!", response.getBody().get("message"));
    }
}

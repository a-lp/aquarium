package fr.upem.devops.controller;

import fr.upem.devops.model.Fish;
import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.model.Schedule;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.PoolActivityService;
import fr.upem.devops.service.ScheduleService;
import fr.upem.devops.service.StaffService;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PoolActivityControllerTest {
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
    }

    @Test
    public void getAll() {
        List lista = this.restTemplate.getForObject("http://localhost:" + port + "/activities", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getById() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/activities", List.class);
        PoolActivity output = this.restTemplate.getForObject("http://localhost:" + port + "/activities/1", PoolActivity.class);
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
        HashMap<String, Object> request = this.restTemplate.postForObject("http://localhost:" + port + "/schedule/1/activities/staff/1,2,3", pa,
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
        HttpEntity<HashMap> updated = new HttpEntity<>(parameters);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/activities/1", HttpMethod.PUT, updated, HashMap.class).getBody();
        assertEquals("1", request.get("id").toString());
        assertEquals("false", request.get("description"));

    }

    @Test
    public void deleteActivity() {
        PoolActivity poolActivity = this.activities.get(0);
        Mockito.when(activityService.remove(poolActivity)).thenReturn(poolActivity);
        PoolActivity response = this.restTemplate.exchange("http://localhost:" + port + "/activities/1", HttpMethod.DELETE, null, PoolActivity.class).getBody();
        assertEquals(Long.valueOf(1L), response.getId());
        assertEquals(poolActivity, response);
    }

    /* HTTP EXCEPTIONS */
    @Test
    public void addPoolActivityScheduleNotFound() {
        Mockito.when(scheduleService.getById(1L)).thenReturn(null);
        ResponseEntity<PoolActivity> response = this.restTemplate.postForEntity("http://localhost:" + port + "/schedule/1/activities/staff/1,2,3", new PoolActivity(),
                PoolActivity.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addPoolActivityResponsibleNotFound() {
        Schedule schedule = new Schedule(1L, new Date(), new Date(), new HashSet<>());
        Mockito.when(scheduleService.getById(1L)).thenReturn(schedule);
        Mockito.when(staffService.getById(1L)).thenReturn(null);
        ResponseEntity<PoolActivity> response = this.restTemplate.postForEntity("http://localhost:" + port + "/schedule/1/activities/staff/1,2,3", new PoolActivity(),
                PoolActivity.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addPoolActivityResponsibleBadRequest() {
        Schedule schedule = new Schedule(1L, new Date(), new Date(), new HashSet<>());
        Mockito.when(scheduleService.getById(1L)).thenReturn(schedule);
        ResponseEntity<PoolActivity> response = this.restTemplate.postForEntity("http://localhost:" + port + "/schedule/1/activities/staff/a,3", new PoolActivity(),
                PoolActivity.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void getByIdNotFound() {
        Mockito.when(activityService.getById(10L)).thenReturn(null);
        ResponseEntity<PoolActivity> response = this.restTemplate.getForEntity("http://localhost:" + port + "/activities/10", PoolActivity.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByIdBadRequest() {
        ResponseEntity<PoolActivity> response = this.restTemplate.getForEntity("http://localhost:" + port + "/activities/asdf", PoolActivity.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateBadRequest() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("startActivity", "wow");
        HttpEntity<HashMap> updated = new HttpEntity<>(parameters);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/activities/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error converting startActivity: '" + parameters.get("startActivity") + "' into LocalTime!", response.getBody().get("message"));
        parameters.remove("startActivity");
        parameters.put("staffList", "a,2");
        updated = new HttpEntity<>(parameters);
        response = this.restTemplate.exchange("http://localhost:" + port + "/activities/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cannot convert staff id: 'a' into Long", response.getBody().get("message"));
        parameters.remove("staffList");
        parameters.put("staffList", "");
        updated = new HttpEntity<>(parameters);
        response = this.restTemplate.exchange("http://localhost:" + port + "/activities/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Activities must have at least one responsible in charge!", response.getBody().get("message"));
    }
}

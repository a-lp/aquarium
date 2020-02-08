package fr.upem.devops.controller;

import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.model.Schedule;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.PoolActivityService;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PoolActivityControllerTest {
    @MockBean
    private PoolActivityService service;
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<PoolActivity> activities = new ArrayList<>();

    @Before
    public void init() {
        PoolActivity pa1 = new PoolActivity(1L, new Date(), new Date(), true, new ArrayList<>(), null);
        PoolActivity pa2 = new PoolActivity(2L, new Date(), new Date(), true, new ArrayList<>(), null);
        PoolActivity pa3 = new PoolActivity(3L, new Date(), new Date(), true, new ArrayList<>(), null);

        activities.add(pa1);
        activities.add(pa2);
        activities.add(pa3);

        Mockito.when(service.getAll()).thenReturn(activities);
        Mockito.when(service.getById(1l)).thenReturn(pa1);
        Mockito.when(service.getById(2l)).thenReturn(pa2);
        Mockito.when(service.getById(3l)).thenReturn(pa3);
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
        PoolActivity pa = new PoolActivity(4L, new Date(), new Date(), true, new ArrayList<>(), null);
        Mockito.when(service.save(pa)).thenReturn(pa);
        PoolActivity request = this.restTemplate.postForObject("http://localhost:" + port + "/activities", pa,
                PoolActivity.class);
        assertEquals(pa.getId(), request.getId());
        assertEquals(pa.getDescription(), request.getDescription());
        assertEquals(pa.getOpenToPublic(), request.getOpenToPublic());
        assertEquals(pa.getSchedule(), request.getSchedule());
        assertEquals(pa.getEndActivity(), request.getEndActivity());
        assertEquals(pa.getStartActivity(), request.getStartActivity());
        assertEquals(pa.getStaffList(), request.getStaffList());
    }

    @Test
    public void assignStaffToPoolActivity() {
        PoolActivity pa = this.activities.get(0);
        Staff s1 = new Staff(1L, "Nome1", "Cognome1", "Address1", new Date(), "SocSec1", Staff.StaffRole.ADMIN);
        pa.assignStaff(s1);
        Mockito.when(service.save(pa)).thenReturn(pa);
        PoolActivity request = this.restTemplate.postForObject("http://localhost:" + port + "/activities/1/assign-staff", pa, PoolActivity.class);
        assertEquals(pa.getId(), request.getId());
        assertEquals(pa.getDescription(), request.getDescription());
        assertEquals(pa.getOpenToPublic(), request.getOpenToPublic());
        assertEquals(pa.getSchedule(), request.getSchedule());
        assertEquals(pa.getEndActivity(), request.getEndActivity());
        assertEquals(pa.getStartActivity(), request.getStartActivity());
        assertEquals(pa.getStaffList(), request.getStaffList());
    }

    @Test
    public void updatePoolActivity() {
        PoolActivity poolActivity = this.activities.get(0);
        Mockito.when(service.save(poolActivity)).thenReturn(poolActivity);
        poolActivity.setDescription("new desc");
        poolActivity.setSchedule(new Schedule());
        HttpEntity<PoolActivity> httpEntity = new HttpEntity<>(poolActivity);
        PoolActivity request = this.restTemplate.exchange("http://localhost:" + port + "/activities/1", HttpMethod.PUT, httpEntity, PoolActivity.class).getBody();
        assertEquals(poolActivity, request);
    }

    @Test
    public void deleteActivity() {
        PoolActivity poolActivity = this.activities.get(0);
        Mockito.when(service.remove(poolActivity)).thenReturn(poolActivity);
        PoolActivity response = this.restTemplate.exchange("http://localhost:" + port + "/activities/1", HttpMethod.DELETE, null, PoolActivity.class).getBody();
        assertEquals(Long.valueOf(1L), response.getId());
        assertEquals(poolActivity, response);
    }
}
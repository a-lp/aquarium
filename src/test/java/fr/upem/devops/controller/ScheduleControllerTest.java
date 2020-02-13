package fr.upem.devops.controller;

import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.model.Schedule;
import fr.upem.devops.service.PoolActivityService;
import fr.upem.devops.service.ScheduleService;
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
@MockBeans({@MockBean(ScheduleService.class), @MockBean(PoolActivityService.class)})
public class ScheduleControllerTest {
    @Autowired
    private ScheduleService service;
    @Autowired
    private PoolActivityService poolActivityService;
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Schedule> schedules = new ArrayList<>();

    @Before
    public void init() {
        for (int i = 0; i < 3; i++) {
            this.schedules.add(new Schedule(Long.parseLong(i + ""), new Date(), new Date(), new ArrayList<>()));
        }
        Mockito.when(service.getAll()).thenReturn(schedules);
        Mockito.when(service.getById(1L)).thenReturn(this.schedules.get(0));
        Mockito.when(service.getById(2L)).thenReturn(this.schedules.get(0));
        Mockito.when(service.getById(3L)).thenReturn(this.schedules.get(0));
    }

    @Test
    public void getAll() {
        List lista = this.restTemplate.getForObject("http://localhost:" + port + "/schedules", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getById() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/schedules", List.class);
        Schedule output = this.restTemplate.getForObject("http://localhost:" + port + "/schedules/1", Schedule.class);
        assertEquals(lista.get(0).get("id").toString(), output.getId().toString());
        assertEquals(((List) (lista.get(0).get("activities"))).size(), output.getActivities().size());
        assertEquals(lista.get(0).get("pool"), output.getPool());
    }

    @Test
    public void addSchedule() {
        Schedule pa = new Schedule(3L, new Date(), new Date(), new ArrayList<>());
        Mockito.when(service.save(pa)).thenReturn(pa);
        Schedule request = this.restTemplate.postForObject("http://localhost:" + port + "/schedules", pa,
                Schedule.class);
        assertEquals(pa.getId(), request.getId());
        assertEquals(pa.getActivities(), request.getActivities());
        assertEquals(pa.getEndPeriod(), request.getEndPeriod());
        assertEquals(pa.getStartPeriod(), request.getStartPeriod());
        assertEquals(pa.getPool(), request.getPool());
    }

    @Test
    public void assignPoolActivityToSchedule() {
        Schedule schedule = this.schedules.get(0);
        PoolActivity pa1 = new PoolActivity(1L, new Date(), new Date(), true, new ArrayList<>(), schedule);
        schedule.assignActivity(pa1);
        Mockito.when(service.save(schedule)).thenReturn(schedule);
        Mockito.when(poolActivityService.getById(1L)).thenReturn(pa1);
        Schedule request = this.restTemplate.postForObject("http://localhost:" + port + "/schedules/1/assign-activity", pa1,
                Schedule.class);
        assertEquals(schedule.getId(), request.getId());
        assertEquals(schedule.getActivities(), request.getActivities());
        assertEquals(schedule.getEndPeriod(), request.getEndPeriod());
        assertEquals(schedule.getStartPeriod(), request.getStartPeriod());
        assertEquals(schedule.getPool(), request.getPool());
        /* Testing bidirectional relation */
        PoolActivity poolActivityRequest = this.restTemplate.getForObject("http://localhost:" + port + "/activities/1", PoolActivity.class);
        assertEquals(pa1.getSchedule(), poolActivityRequest.getSchedule());
    }

    @Test
    public void updateSchedule() {
        Schedule schedule = this.schedules.get(0);
        Mockito.when(service.save(schedule)).thenReturn(schedule);
        PoolActivity pa1 = new PoolActivity(1L, new Date(), new Date(), true, new ArrayList<>(), schedule);
        schedule.assignActivity(pa1);
        HttpEntity<Schedule> httpEntity = new HttpEntity<>(schedule);
        Schedule request = this.restTemplate.exchange("http://localhost:" + port + "/schedules/1", HttpMethod.PUT, httpEntity, Schedule.class).getBody();
        assertEquals(schedule, request);
        assertEquals(schedule.getActivities(), request.getActivities());
        /* Testing bidirectional relation */
        Mockito.when(poolActivityService.getById(1L)).thenReturn(pa1);
        PoolActivity poolActivityRequest = this.restTemplate.getForObject("http://localhost:" + port + "/activities/1", PoolActivity.class);
        assertEquals(poolActivityRequest.getSchedule(), schedule);
    }

    @Test
    public void deleteSchedule() {
        Schedule schedule = this.schedules.get(0);
        Mockito.when(service.remove(schedule)).thenReturn(schedule);
        Schedule request = this.restTemplate.exchange("http://localhost:" + port + "/schedules/1", HttpMethod.DELETE, null, Schedule.class).getBody();
        assertEquals(schedule, request);
    }
}
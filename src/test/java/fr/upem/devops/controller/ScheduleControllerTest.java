package fr.upem.devops.controller;

import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.model.Schedule;
import fr.upem.devops.service.ScheduleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScheduleControllerTest {
    @MockBean
    private ScheduleService service;
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Schedule> schedules = new ArrayList<>();

    @Before
    public void init() {
        for (int i = 0; i < 3; i++) {
            this.schedules.add(new Schedule(Long.parseLong(i + ""), new Date(), new Date(), (i % 2 == 0), new ArrayList<>()));
        }
        Mockito.when(service.getAll()).thenReturn(schedules);
        Mockito.when(service.getById(1l)).thenReturn(this.schedules.get(0));
        Mockito.when(service.getById(2l)).thenReturn(this.schedules.get(0));
        Mockito.when(service.getById(3l)).thenReturn(this.schedules.get(0));
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
        assertEquals(lista.get(0).get("repeated"), output.getRepeated());
        assertEquals(lista.get(0).get("pool"), output.getPool());
    }

    @Test
    public void addSchedule() {
        Schedule pa = new Schedule(3L, new Date(), new Date(), false, new ArrayList<>());
        Mockito.when(service.save(pa)).thenReturn(pa);
        Schedule request = this.restTemplate.postForObject("http://localhost:" + port + "/schedules", pa,
                Schedule.class);
        assertEquals(pa.getId(), request.getId());
        assertEquals(pa.getActivities(), request.getActivities());
        assertEquals(pa.getRepeated(), request.getRepeated());
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
        Schedule request = this.restTemplate.postForObject("http://localhost:" + port + "/schedules/1/assign-activity", pa1,
                Schedule.class);
        assertEquals(schedule.getId(), request.getId());
        assertEquals(schedule.getActivities(), request.getActivities());
        assertEquals(schedule.getRepeated(), request.getRepeated());
        assertEquals(schedule.getEndPeriod(), request.getEndPeriod());
        assertEquals(schedule.getStartPeriod(), request.getStartPeriod());
        assertEquals(schedule.getPool(), request.getPool());
    }
}
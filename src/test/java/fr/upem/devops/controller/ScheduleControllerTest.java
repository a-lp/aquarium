package fr.upem.devops.controller;

import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Schedule;
import fr.upem.devops.service.PoolActivityService;
import fr.upem.devops.service.PoolService;
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

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBeans({@MockBean(ScheduleService.class), @MockBean(PoolActivityService.class)})
public class ScheduleControllerTest {
    @MockBean
    private ScheduleService service;
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
        Pool p1 = new Pool(1L, 10L, 10.5, Pool.WaterCondition.CLEAN, new ArrayList<>());
        Pool p2 = new Pool(2L, 20L, 20.5, Pool.WaterCondition.CLEAN, new ArrayList<>());
        Pool p3 = new Pool(3L, 30L, 30.5, Pool.WaterCondition.DIRTY, new ArrayList<>());
        pools.addAll(Arrays.asList(p1, p2, p3));
        for (int i = 0; i < 3; i++) {
            this.schedules.add(new Schedule(Long.parseLong(i + ""), new Date(), new Date(), new ArrayList<>()));
            this.schedules.get(i).setPool(pools.get(i));
            pools.get(i).getSchedules().add(this.schedules.get(i));
        }
        Mockito.when(service.getAll()).thenReturn(schedules);
        Mockito.when(service.getById(1L)).thenReturn(this.schedules.get(0));
        Mockito.when(service.getById(2L)).thenReturn(this.schedules.get(1));
        Mockito.when(service.getById(3L)).thenReturn(this.schedules.get(2));
        Mockito.when(poolService.getById(1L)).thenReturn(this.pools.get(0));
        Mockito.when(poolService.getById(2L)).thenReturn(this.pools.get(1));
        Mockito.when(poolService.getById(3L)).thenReturn(this.pools.get(2));
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
        assertEquals(((LinkedHashMap) lista.get(0).get("pool")).get("id").toString(), output.getPool().getId().toString());
    }

    @Test
    public void addSchedule() {
        Schedule schedule = new Schedule(new Date(), new Date(), new ArrayList<>());
        Schedule schedule_new = new Schedule(4L, new Date(), new Date(), new ArrayList<>());
        Pool pool = this.pools.get(0);
        schedule_new.setPool(pool);
        pool.getSchedules().add(schedule_new);
        Mockito.when(service.save(schedule)).thenReturn(schedule_new);
        Schedule request = this.restTemplate.postForObject("http://localhost:" + port + "/pools/1/schedules", schedule,
                Schedule.class);
        assertEquals(schedule_new.getId(), request.getId());
        assertEquals(schedule_new.getScheduledActivities(), request.getScheduledActivities());
        assertEquals(schedule_new.getEndPeriod(), request.getEndPeriod());
        assertEquals(schedule_new.getStartPeriod(), request.getStartPeriod());
        assertEquals(schedule_new.getPool(), request.getPool());
    }

    @Test
    public void addSchedulePoolNotFound() {
        Mockito.when(poolService.getById(2L)).thenThrow(new ResourceNotFoundException("Pool with id '4' not found!"));
        Schedule schedule = new Schedule(new Date(), new Date(), new ArrayList<>());
        ResourceNotFoundException request = this.restTemplate.postForObject("http://localhost:" + port + "/pools/4/schedules", schedule,
                ResourceNotFoundException.class);
        assertEquals("Pool with id '4' not found!", request.getMessage());
    }


    @Test
    public void updateSchedule() {
        Schedule schedule = this.schedules.get(0);
        Mockito.when(service.save(schedule)).thenReturn(schedule);
        schedule.setPool(this.pools.get(2));
        HttpEntity<Schedule> httpEntity = new HttpEntity<>(schedule);
        Schedule request = this.restTemplate.exchange("http://localhost:" + port + "/schedules/1", HttpMethod.PUT, httpEntity, Schedule.class).getBody();
        assertEquals(schedule, request);
    }

    @Test
    public void deleteSchedule() {
        Schedule schedule = this.schedules.get(0);
        Mockito.when(service.remove(schedule)).thenReturn(schedule);
        Schedule request = this.restTemplate.exchange("http://localhost:" + port + "/schedules/1", HttpMethod.DELETE, null, Schedule.class).getBody();
        assertEquals(schedule, request);
    }
}
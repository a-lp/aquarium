package fr.upem.devops.controller;

import fr.upem.devops.model.Pool;
import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.model.Sector;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.StaffService;
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

import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StaffControllerTest {
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
        Pool p1 = new Pool(1L, 10L, 10.5, Pool.WaterCondition.CLEAN, new ArrayList<>());
        Pool p2 = new Pool(2L, 20L, 20.5, Pool.WaterCondition.CLEAN, new ArrayList<>());
        Pool p3 = new Pool(3L, 30L, 30.5, Pool.WaterCondition.DIRTY, new ArrayList<>());
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
    }

    @Test
    public void getAll() {
        List lista = this.restTemplate.getForObject("http://localhost:" + port + "/staff", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getById() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/staff", List.class);
        Staff output = this.restTemplate.getForObject("http://localhost:" + port + "/staff/1", Staff.class);
        assertEquals(lista.get(0).get("id").toString(), output.getId().toString());
        assertEquals(lista.get(0).get("name").toString(), output.getName());
        assertEquals(lista.get(0).get("surname"), output.getSurname());
//        assertEquals(lista.get(0).get("birthday"), output.getBirthday());
        assertEquals(lista.get(0).get("socialSecurity"), output.getSocialSecurity());
        assertEquals(Staff.StaffRole.valueOf(lista.get(0).get("role").toString()), output.getRole());
        assertEquals(((List) lista.get(0).get("poolsResponsabilities")).size(), output.getPoolsResponsabilities().size());
        assertEquals(((List) lista.get(0).get("sectors")).size(), output.getSectors().size());
    }

    @Test
    public void addStaff() {
        Date date = new Date();
        Staff staff = new Staff(4L, "Nome4", "Cognome4", "Address4", date, "SocSec4", Staff.StaffRole.WORKER);
        Mockito.when(staffService.save(staff)).thenReturn(new Staff(4L, "Nome4", "Cognome4", "Address4", date, "SocSec4", Staff.StaffRole.WORKER));

        Staff request = this.restTemplate.postForObject("http://localhost:" + port + "/staff", staff,
                Staff.class);
        assertEquals(request.getId(), staff.getId());
        assertEquals(request.getName(), staff.getName());
        assertEquals(request.getSurname(), staff.getSurname());
//        assertEquals(request.getBirthday(), staff.getBirthday());
        assertEquals(request.getSocialSecurity(), staff.getSocialSecurity());
        assertEquals(request.getRole(), staff.getRole());
        assertEquals(request.getPoolsResponsabilities().size(), staff.getPoolsResponsabilities().size());
        assertEquals(request.getSectors().size(), staff.getSectors().size());
    }

    @Test
    public void assignPoolToStaff() {
        Staff staff = this.staffList.get(0);
        Pool pool = new Pool(4L, 4L, 4.0, Pool.WaterCondition.CLEAN, null);
        staff.assignPool(pool);
        Staff response = staff;
        response.assignPool(pool);
        Mockito.when(staffService.save(staff)).thenReturn(response);
        Staff request = this.restTemplate.postForObject("http://localhost:" + port + "/staff", staff,
                Staff.class);
        assertEquals(request.getId(), staff.getId());
        assertEquals(request.getName(), staff.getName());
        assertEquals(request.getSurname(), staff.getSurname());
//        assertEquals(request.getBirthday(), staff.getBirthday());
        assertEquals(request.getSocialSecurity(), staff.getSocialSecurity());
        assertEquals(request.getRole(), staff.getRole());
        assertEquals(request.getPoolsResponsabilities().size(), staff.getPoolsResponsabilities().size());
        assertEquals(request.getSectors().size(), staff.getSectors().size());

    }

    @Test
    public void assignSectorToStaff() {
        Staff staff = this.staffList.get(0);
        Sector sec = new Sector(4L, "Sector4", "Location4");
        staff.assignSector(sec);
        Staff response = staff;
        response.assignSector(sec);
        Mockito.when(staffService.save(staff)).thenReturn(response);
        Staff request = this.restTemplate.postForObject("http://localhost:" + port + "/staff", staff,
                Staff.class);
        assertEquals(request.getId(), staff.getId());
        assertEquals(request.getName(), staff.getName());
        assertEquals(request.getSurname(), staff.getSurname());
//        assertEquals(request.getBirthday(), staff.getBirthday());
        assertEquals(request.getSocialSecurity(), staff.getSocialSecurity());
        assertEquals(request.getRole(), staff.getRole());
        assertEquals(request.getPoolsResponsabilities().size(), staff.getPoolsResponsabilities().size());
        assertEquals(request.getSectors().size(), staff.getSectors().size());
    }

    @Test
    public void updateStaff() {
        Staff staff = this.staffList.get(0);
        Mockito.when(staffService.save(staff)).thenReturn(staff);
        staff.setSurname("New Surname");
        staff.assignSector(new Sector());
        HttpEntity<Staff> httpEntity = new HttpEntity<>(staff);
        Staff request = this.restTemplate.exchange("http://localhost:" + port + "/staff/1", HttpMethod.PUT, httpEntity, Staff.class).getBody();
        assertEquals(staff, request);
    }

    @Test
    public void deleteStaff() {
        Staff staff = this.staffList.get(0);
        Mockito.when(staffService.remove(staff)).thenReturn(staff);
        Staff request = this.restTemplate.exchange("http://localhost:" + port + "/staff/1", HttpMethod.DELETE, null, Staff.class).getBody();
        assertEquals(staff, request);
    }

    @Test
    public void assignActivityToStaff() {
        Staff staff = this.staffList.get(0);
        PoolActivity activity = new PoolActivity(1L, LocalTime.of(1,0),LocalTime.of(2,0), true, Collections.singletonList(staff), null);
        staff.assignActivity(activity);
        Staff response = staff;
        response.assignActivity(activity);
        Mockito.when(staffService.save(staff)).thenReturn(response);
        Staff request = this.restTemplate.postForObject("http://localhost:" + port + "/staff", staff,
                Staff.class);
        assertEquals(request.getId(), staff.getId());
        assertEquals(request.getName(), staff.getName());
        assertEquals(request.getSurname(), staff.getSurname());
        assertEquals(request.getBirthday().getTime(), staff.getBirthday().getTime());
        assertEquals(request.getSocialSecurity(), staff.getSocialSecurity());
        assertEquals(request.getRole(), staff.getRole());
        assertEquals(request.getPoolsResponsabilities().size(), staff.getPoolsResponsabilities().size());
        assertEquals(request.getSectors().size(), staff.getSectors().size());
        assertEquals(request.getActivities().size(), staff.getActivities().size());
    }
}
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

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
    }

    @Test
    public void getAll() {
        List lista = this.restTemplate.getForObject("http://localhost:" + port + "/staff", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getById() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/staff", List.class);
        HashMap<String, Object> output = this.restTemplate.getForObject("http://localhost:" + port + "/staff/1", HashMap.class);
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

        Staff request = this.restTemplate.postForObject("http://localhost:" + port + "/staff", staff,
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
        HttpEntity<HashMap> httpEntity = new HttpEntity<>(parameters);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/staff/1", HttpMethod.PUT, httpEntity, HashMap.class).getBody();
        assertEquals(staff.getId().toString(), request.get("id").toString());
        assertEquals(staff.getSurname(), request.get("surname"));
        assertEquals(staff.getSocialSecurity(), request.get("socialSecurity"));
    }

    @Test
    public void deleteStaff() {
        Staff staff = this.staffList.get(0);
        Mockito.when(staffService.remove(staff)).thenReturn(staff);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/staff/1", HttpMethod.DELETE, null, HashMap.class).getBody();
        assertEquals(staff.getId().toString(), request.get("id").toString());
    }


    @Test
    public void getByIdNotFound() {
        Mockito.when(staffService.getById(10L)).thenReturn(null);
        ResponseEntity<Staff> response = this.restTemplate.getForEntity("http://localhost:" + port + "/staff/10", Staff.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByIdBadRequest() {
        ResponseEntity<Staff> response = this.restTemplate.getForEntity("http://localhost:" + port + "/staff/asdf", Staff.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateBadRequest() {
        Mockito.when(staffService.getById(1L)).thenReturn(this.staffList.get(0));
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("birthday", "ww");
        HttpEntity<HashMap> updated = new HttpEntity<>(parameters);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/staff/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("'" + parameters.get("birthday") + "' is not a valid value to be converted in date. Insert a Long representing the time in milliseconds", response.getBody().get("message"));
        parameters.remove("birthday");
        parameters.put("role", "zxcv");
        updated = new HttpEntity<>(parameters);
        response = this.restTemplate.exchange("http://localhost:" + port + "/staff/1", HttpMethod.PUT,
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
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/staff/10", HttpMethod.DELETE, null, HashMap.class);
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
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/staff/10", HttpMethod.DELETE, null, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sectors must have at least one responsible! Removing this staff, will also remove the only one responsible", response.getBody().get("message"));
    }
}

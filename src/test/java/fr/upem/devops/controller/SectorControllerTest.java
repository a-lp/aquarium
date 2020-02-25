package fr.upem.devops.controller;

import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Sector;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.SectorService;
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

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectorControllerTest {
    @MockBean
    private SectorService sectorService;

    @MockBean
    private StaffService staffService;

    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Sector> sectors = new ArrayList<>();
    private List<Staff> staff = new ArrayList<>();

    @Before
    public void init() {
        Sector s1 = new Sector(1L, "Sector1", "Location1");
        Sector s2 = new Sector(2L, "Sector2", "Location2");
        Sector s3 = new Sector(3L, "Sector3", "Location3");
        Staff st1 = new Staff(1L, "Nome1", "Cognome1", "Address1", new Date(), "SocSec1", Staff.StaffRole.ADMIN);
        Staff st2 = new Staff(2L, "Nome2", "Cognome2", "Address2", new Date(), "SocSec2", Staff.StaffRole.MANAGER);
        Staff st3 = new Staff(3L, "Nome3", "Cognome3", "Address3", new Date(), "SocSec3", Staff.StaffRole.WORKER);
        Pool a1 = new Pool(1L, 1L, 1.0, Pool.WaterCondition.DIRTY, new HashSet<>());
        Pool a2 = new Pool(2L, 2L, 2.0, Pool.WaterCondition.CLEAN, new HashSet<>());
        Pool a3 = new Pool(3L, 3L, 3.0, Pool.WaterCondition.DIRTY, new HashSet<>());

        s1.addPool(a1);
        s2.addPool(a2);
        s3.addPool(a3);

        sectors.addAll(Arrays.asList(s1, s2, s3));
        staff.addAll(Arrays.asList(st1, st2, st3));

        Mockito.when(sectorService.getAll()).thenReturn(sectors);
        Mockito.when(sectorService.getByName("Sector1")).thenReturn(s1);
        Mockito.when(sectorService.getByName("Sector2")).thenReturn(s2);

        Mockito.when(staffService.getById(1L)).thenReturn(st1);
        Mockito.when(staffService.getById(2L)).thenReturn(st2);
        Mockito.when(staffService.getById(3L)).thenReturn(st3);
    }

    @Test
    public void getAll() {
        List lista = this.restTemplate.getForObject("http://localhost:" + port + "/sectors", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getByName() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/sectors", List.class);
        HashMap<String, Object> request = this.restTemplate.getForObject("http://localhost:" + port + "/sectors/Sector1", HashMap.class);
        assertEquals(lista.get(0).get("id").toString(), request.get("id").toString());
        assertEquals(lista.get(0).get("name"), request.get("name").toString());
        assertEquals(lista.get(0).get("location"), request.get("location").toString());
    }

    @Test
    public void addSector() {
        Sector sec = new Sector("Sector4", "Location4");
        Sector sec_new = new Sector(4L, "Sector4", "Location4");
        sec_new.setStaffList(Sets.newHashSet(this.staff));
        Mockito.when(sectorService.save(sec)).thenReturn(sec_new);
        HashMap<String, Object> request = this.restTemplate.postForObject("http://localhost:" + port + "/sectors/responsible/1,2,3", sec,
                HashMap.class);
        assertEquals(sec_new.getId().toString(), request.get("id").toString());
        assertEquals(sec_new.getName(), request.get("name").toString());
        assertEquals(sec_new.getLocation(), request.get("location").toString());
        assertEquals(sec_new.getPools().size(), ((List) request.get("pools")).size());
        assertEquals(sec_new.getStaffList().size(), ((List) request.get("staffList")).size());
    }

    @Test
    public void updateSector() {
        Sector sector = this.sectors.get(0);
        Mockito.when(sectorService.getById(1L)).thenReturn(sector);
        Mockito.when(sectorService.save(sector)).thenReturn(sector);
        sector.setName("New Name");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", sector.getName());
        parameters.put("location", sector.getLocation());
        HttpEntity<HashMap> httpEntity = new HttpEntity<>(parameters);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/sectors/1", HttpMethod.PUT, httpEntity, HashMap.class).getBody();
        assertEquals(sector.getId().toString(), request.get("id").toString());
        assertEquals(sector.getName(), request.get("name"));
    }

    @Test
    public void deleteSector() {
        Sector sector = new Sector();
        sector.setId(100L);
        Mockito.when(sectorService.getByName("SectorNew")).thenReturn(sector);
        Mockito.when(sectorService.remove(sector)).thenReturn(sector);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/sectors/SectorNew", HttpMethod.DELETE, null, HashMap.class).getBody();
        assertEquals(sector.getId().toString(), request.get("id").toString());
    }


    @Test
    public void getByIdNotFound() {
        Mockito.when(sectorService.getById(10L)).thenReturn(null);
        ResponseEntity<Sector> response = this.restTemplate.getForEntity("http://localhost:" + port + "/sectors/id/10", Sector.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByNameNotFound() {
        Mockito.when(sectorService.getByName("10")).thenReturn(null);
        ResponseEntity<Sector> response = this.restTemplate.getForEntity("http://localhost:" + port + "/sectors/10", Sector.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByIdBadRequest() {
        ResponseEntity<Sector> response = this.restTemplate.getForEntity("http://localhost:" + port + "/sectors/id/asdf", Sector.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void addSectorResponsibleNotFound() {
        Mockito.when(staffService.getById(4L)).thenReturn(null);
        ResponseEntity<Sector> response = this.restTemplate.postForEntity("http://localhost:" + port + "/sectors/responsible/4", new Sector(),
                Sector.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addSectorDuplicatedName() {
        Sector sector = this.sectors.get(0);
        sector.setPools(null);
        Mockito.when(sectorService.getByName(sector.getName())).thenReturn(sector);
        ResponseEntity<HashMap> response = this.restTemplate.postForEntity("http://localhost:" + port + "/sectors/responsible/1", sector,
                HashMap.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Duplicate name for sector '" + sector.getName(), response.getBody().get("message"));
    }

    @Test
    public void updateBadRequest() {
        Mockito.when(sectorService.getById(1L)).thenReturn(this.sectors.get(0));
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("staffList", "a,2");
        HttpEntity<HashMap> updated = new HttpEntity<>(parameters);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/sectors/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error during conversion of staff ids in Long!", response.getBody().get("message"));
        parameters.put("staffList", "");
        updated = new HttpEntity<>(parameters);
        response = this.restTemplate.exchange("http://localhost:" + port + "/sectors/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cannot update sector, no staff found! There must be at least one responsible", response.getBody().get("message"));
    }

    @Test
    public void deleteSectorBadRequest() {
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/sectors/Sector1", HttpMethod.DELETE, null, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cannot delete sector! There are pools in this sector.", response.getBody().get("message"));
    }
}


package fr.upem.devops.controller;

import fr.upem.devops.errors.ConflictException;
import fr.upem.devops.errors.ResourceNotFoundException;
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
        Pool a1 = new Pool(1L, 1L, 1.0, Pool.WaterCondition.DIRTY, null);
        Pool a2 = new Pool(2L, 2L, 2.0, Pool.WaterCondition.CLEAN, null);
        Pool a3 = new Pool(3L, 3L, 3.0, Pool.WaterCondition.DIRTY, null);

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
        Sector output = this.restTemplate.getForObject("http://localhost:" + port + "/sectors/Sector1", Sector.class);
        assertEquals(lista.get(0).get("id").toString(), output.getId().toString());
        assertEquals(lista.get(0).get("name"), output.getName());
        assertEquals(lista.get(0).get("location"), output.getLocation());
        assertEquals(((List<Pool>) lista.get(0).get("pools")).size(), output.getPools().size());
    }

    @Test
    public void addSector() {
        Sector sec = new Sector("Sector4", "Location4");
        Sector sec_new = new Sector(4L, "Sector4", "Location4");
        sec_new.setStaffList(Sets.newHashSet(this.staff));
        Mockito.when(sectorService.save(sec)).thenReturn(sec_new);
        Sector request = this.restTemplate.postForObject("http://localhost:" + port + "/sectors/responsible/1,2,3", sec,
                Sector.class);
        assertEquals(sec_new.getId(), request.getId());
        assertEquals(sec_new.getName(), request.getName());
        assertEquals(sec_new.getLocation(), request.getLocation());
        assertEquals(sec_new.getPools(), request.getPools());
        assertEquals(sec_new.getStaffList(), request.getStaffList());
    }

    @Test
    public void addSectorResponsibleNotFound() {
        Mockito.when(staffService.getById(4L)).thenThrow(new ResourceNotFoundException("Staff 4 not found!"));
        Sector sec = new Sector("Sector4", "Location4");
        ResourceNotFoundException request = this.restTemplate.postForObject("http://localhost:" + port + "/sectors/responsible/4", sec,
                ResourceNotFoundException.class);
        assertEquals("Staff 4 not found!", request.getMessage());
    }

    @Test
    public void addSectorDuplicatedName() {
        Sector sec = new Sector("Sector1", "Location4");
        Mockito.when(sectorService.save(sec)).thenThrow(new ConflictException("Another sector named 'Sector1' found!"));
        ConflictException request = this.restTemplate.postForObject("http://localhost:" + port + "/sectors/responsible/1", sec,
                ConflictException.class);
        assertEquals("Another sector named 'Sector1' found!", request.getMessage());
    }

    @Test
    public void updateSector() {
        Sector sector = this.sectors.get(0);
        Mockito.when(sectorService.save(sector)).thenReturn(sector);
        sector.addPool(new Pool());
        HttpEntity<Sector> httpEntity = new HttpEntity<>(sector);
        Sector request = this.restTemplate.exchange("http://localhost:" + port + "/sectors/Sector1", HttpMethod.PUT, httpEntity, Sector.class).getBody();
        assertEquals(sector, request);
        assertEquals(sector.getPools(), request.getPools());
    }

    @Test
    public void deleteSector() {
        Sector sector = this.sectors.get(0);
        Mockito.when(sectorService.remove(sector)).thenReturn(sector);
        Sector request = this.restTemplate.exchange("http://localhost:" + port + "/sectors/Sector1", HttpMethod.DELETE, null, Sector.class).getBody();
        assertEquals(sector, request);
    }
}

package fr.upem.devops.controller;

import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.*;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.SectorService;
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

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PoolControllerTest {
    @MockBean
    private PoolService poolService;
    @MockBean
    private SectorService sectorService;
    @MockBean
    private StaffService staffService;
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Pool> pools = new ArrayList<>();
    private List<Sector> sectors = new ArrayList<>();

    @Before
    public void init() {
        Pool p1 = new Pool(1L, 10L, 10.5, Pool.WaterCondition.CLEAN, new HashSet<>());
        Pool p2 = new Pool(2L, 20L, 20.5, Pool.WaterCondition.CLEAN, new HashSet<>());
        Pool p3 = new Pool(3L, 30L, 30.5, Pool.WaterCondition.DIRTY, new HashSet<>());
        Sector s1 = new Sector(1L, "Sector1", "Location1");
        Sector s2 = new Sector(2L, "Sector2", "Location2");
        Sector s3 = new Sector(3L, "Sector3", "Location3");
        Fish a1 = new Fish(1L, "Shark", FishGender.HERMAPHRODITE, "forti mascelle e di dimensioni medio-grandi", null, p1);
        Fish a2 = new Fish(2L, "Codfish", FishGender.MALE, "buono da fare al forno", null, p2);
        Fish a3 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", null, p3);

        p1.addFish(a1);
        p2.addFish(a2);
        p3.addFish(a3);
        p1.setSector(s1);
        p2.setSector(s2);
        p3.setSector(s3);
        s1.addPool(p1);
        s2.addPool(p2);
        s3.addPool(p3);

        pools.addAll(Arrays.asList(p1, p2, p3));
        sectors.addAll(Arrays.asList(s1, s2, s3));
        Mockito.when(poolService.getAll()).thenReturn(pools);
        Mockito.when(poolService.getById(1L)).thenReturn(p1);
        Mockito.when(poolService.getById(2L)).thenReturn(p2);
        Mockito.when(poolService.getById(3L)).thenReturn(p3);
        Mockito.when(sectorService.getByName("Sector1")).thenReturn(s1);
        Mockito.when(sectorService.getByName("Sector2")).thenReturn(s2);
        Mockito.when(sectorService.getByName("Sector3")).thenReturn(s3);
    }

    @Test
    public void getAll() {
        Set Seta = this.restTemplate.getForObject("http://localhost:" + port + "/pools", Set.class);
        assertEquals(3, Seta.size());
    }

    @Test
    public void getById() {
        List<HashMap> poolSet = this.restTemplate.getForObject("http://localhost:" + port + "/pools", List.class);
        HashMap<String, Object> request = this.restTemplate.getForObject("http://localhost:" + port + "/pools/1", HashMap.class);
        assertEquals(poolSet.get(0).get("id").toString(), request.get("id").toString());
        assertEquals(poolSet.get(0).get("maxCapacity").toString(), request.get("maxCapacity").toString());
        assertEquals(poolSet.get(0).get("volume"), request.get("volume"));
        assertEquals(poolSet.get(0).get("condition"), request.get("condition"));
        assertEquals(((ArrayList<Fish>) (poolSet.get(0).get("fishes"))).size(), ((List) request.get("fishes")).size());
    }

    @Test
    public void addPool() {
        Pool pool = new Pool(4L, 40L, 40.5, Pool.WaterCondition.DIRTY, new HashSet<>());
        Sector sector = sectors.get(0);
        Pool pool_new = new Pool(4L, 40L, 40.5, Pool.WaterCondition.DIRTY, new HashSet<>());
        pool_new.setSector(sector);
        sector.addPool(pool_new);
        Mockito.when(sectorService.getById(1L)).thenReturn(sector);
        Staff s1 = new Staff(1L, "Nome1", "Cognome1", "Address1", new Date(), "SocSec1", Staff.StaffRole.ADMIN);
        pool_new.setResponsible(s1);
        Mockito.when(staffService.getById(1L)).thenReturn(s1);
        Mockito.when(poolService.save(pool)).thenReturn(pool_new);
        HashMap<String, Object> request = this.restTemplate.postForObject("http://localhost:" + port + "/sectors/1/responsible/1/pools", pool,
                HashMap.class);
        assertEquals(pool.getId().toString(), request.get("id").toString());
        assertEquals(pool.getCondition().name(), request.get("condition"));
        assertEquals(pool.getMaxCapacity().toString(), request.get("maxCapacity").toString());
        assertEquals(pool.getVolume().toString(), request.get("volume").toString());
        assertEquals(pool.getFishes().size(), ((List) request.get("fishes")).size());
        assertEquals(pool_new.getSector().getName(), request.get("sector"));
    }

    @Test
    public void addPoolSectorNotFound() {
        Mockito.when(sectorService.getById(4L)).thenThrow(new ResourceNotFoundException("Sector with id '4' not found!"));
        Pool pool = new Pool(4L, 40L, 40.5, Pool.WaterCondition.DIRTY, new HashSet<>());
        ResourceNotFoundException request = this.restTemplate.postForObject("http://localhost:" + port + "/sectors/4/responsible/1/pools", pool,
                ResourceNotFoundException.class);
        assertEquals("Sector with id '4' not found!", request.getMessage());
    }

    @Test
    public void addPoolResponsibleNotFound() {
        Mockito.when(sectorService.getById(1L)).thenReturn(sectors.get(0));
        Mockito.when(staffService.getById(2L)).thenThrow(new ResourceNotFoundException("Staff '2' not found!"));
        Pool pool = new Pool(4L, 40L, 40.5, Pool.WaterCondition.DIRTY, new HashSet<>());
        ResourceNotFoundException request = this.restTemplate.postForObject("http://localhost:" + port + "/sectors/1/responsible/2/pools", pool,
                ResourceNotFoundException.class);
        assertEquals("Staff '2' not found!", request.getMessage());
    }


    @Test
    public void updatePool() {
        Pool pool = this.pools.get(0);
        Mockito.when(poolService.save(pool)).thenReturn(pool);
        pool.setCondition(Pool.WaterCondition.DIRTY);
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", pool.getId().toString());
        parameters.put("maxCapacity", pool.getMaxCapacity().toString());
        parameters.put("volume", pool.getVolume().toString());
        parameters.put("condition", pool.getCondition().name());
        HttpEntity<HashMap> httpEntity = new HttpEntity<>(parameters);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/pools/1", HttpMethod.PUT, httpEntity, HashMap.class).getBody();
        assertEquals(pool.getId().toString(), request.get("id").toString());
        assertEquals(pool.getCondition().name(), request.get("condition"));
    }

    @Test
    public void deletePool() {
        Pool pool = this.pools.get(0);
        Mockito.when(poolService.remove(pool)).thenReturn(pool);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/pools/1", HttpMethod.DELETE, null, HashMap.class).getBody();
        assertEquals(pool.getId().toString(), request.get("id").toString());
    }
}

package fr.upem.devops.controller;

import fr.upem.devops.model.Fish;
import fr.upem.devops.model.FishGender;
import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Sector;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.SectorService;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PoolControllerTest {
    @MockBean
    private PoolService poolService;
    @MockBean
    private SectorService sectorService;
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Pool> pools = new ArrayList<>();
    private List<Sector> sectors = new ArrayList<>();

    @Before
    public void init() {
        Pool p1 = new Pool(1L, 10L, 10.5, Pool.WaterCondition.CLEAN, new ArrayList<>());
        Pool p2 = new Pool(2L, 20L, 20.5, Pool.WaterCondition.CLEAN, new ArrayList<>());
        Pool p3 = new Pool(3L, 30L, 30.5, Pool.WaterCondition.DIRTY, new ArrayList<>());
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
        List lista = this.restTemplate.getForObject("http://localhost:" + port + "/pools", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getById() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/pools", List.class);
        Pool output = this.restTemplate.getForObject("http://localhost:" + port + "/pools/1", Pool.class);
        assertEquals(lista.get(0).get("id").toString(), output.getId().toString());
        assertEquals(lista.get(0).get("maxCapacity").toString(), output.getMaxCapacity().toString());
        assertEquals(lista.get(0).get("volume"), output.getVolume());
        assertEquals(lista.get(0).get("condition"), output.getCondition().name());
        assertEquals(((List<Fish>) (lista.get(0).get("fishes"))).size(), output.getFishes().size());
    }

    @Test
    public void addPool() {
        Pool pool = new Pool(4L, 40L, 40.5, Pool.WaterCondition.DIRTY, new ArrayList<>());
        Sector sector = sectors.get(0);
        Pool pool_new = new Pool(4L, 40L, 40.5, Pool.WaterCondition.DIRTY, new ArrayList<>());
        pool_new.setSector(sector);
        sector.addPool(pool_new);
        Mockito.when(sectorService.getById(1L)).thenReturn(sector);
        Mockito.when(poolService.save(pool)).thenReturn(pool_new);
        Pool request = this.restTemplate.postForObject("http://localhost:" + port + "/sectors/1/pools", pool,
                Pool.class);
        assertEquals(pool.getId(), request.getId());
        assertEquals(pool.getCondition(), request.getCondition());
        assertEquals(pool.getMaxCapacity(), request.getMaxCapacity());
        assertEquals(pool.getVolume(), request.getVolume());
        assertEquals(pool.getFishes(), request.getFishes());
        assertEquals(pool_new.getSector(), request.getSector());
    }


    @Test
    public void updatePool() {
        Pool pool = this.pools.get(0);
        Mockito.when(poolService.save(pool)).thenReturn(pool);
        pool.addFish(new Fish());
        HttpEntity<Pool> httpEntity = new HttpEntity<>(pool);
        Pool request = this.restTemplate.exchange("http://localhost:" + port + "/pools/1", HttpMethod.PUT, httpEntity, Pool.class).getBody();
        assertEquals(pool, request);
        assertEquals(pool.getFishes(), request.getFishes());
    }

    @Test
    public void deletePool() {
        Pool pool = this.pools.get(0);
        Mockito.when(poolService.remove(pool)).thenReturn(pool);
        Pool request = this.restTemplate.exchange("http://localhost:" + port + "/pools/1", HttpMethod.DELETE, null, Pool.class).getBody();
        assertEquals(pool, request);
    }
}
package fr.upem.devops.controller;

import fr.upem.devops.model.Fish;
import fr.upem.devops.model.FishGender;
import fr.upem.devops.model.Pool;
import fr.upem.devops.service.PoolService;
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
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PoolControllerTest {
    @MockBean
    private PoolService poolService;
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Pool> pools = new ArrayList<>();

    @Before
    public void init() {
        Pool s1 = new Pool(1L, 10L, 10.5, Pool.WaterCondition.CLEAN, new ArrayList<>());
        Pool s2 = new Pool(2L, 20L, 20.5, Pool.WaterCondition.CLEAN, new ArrayList<>());
        Pool s3 = new Pool(3L, 30L, 30.5, Pool.WaterCondition.DIRTY, new ArrayList<>());

        Fish a1 = new Fish(1L, "Shark", FishGender.HERMAPHRODITE, "forti mascelle e di dimensioni medio-grandi", null, s1);
        Fish a2 = new Fish(2L, "Codfish", FishGender.MALE, "buono da fare al forno", null, s2);
        Fish a3 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", null, s3);

        s1.addFish(a1);
        s2.addFish(a2);
        s3.addFish(a3);

        pools.add(s1);
        pools.add(s2);
        pools.add(s3);
        Mockito.when(poolService.getAll()).thenReturn(pools);
        Mockito.when(poolService.getById(1l)).thenReturn(s1);
        Mockito.when(poolService.getById(2l)).thenReturn(s2);
        Mockito.when(poolService.getById(3l)).thenReturn(s3);
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
        Mockito.when(poolService.save(pool)).thenReturn(new Pool(4L, 40L, 40.5, Pool.WaterCondition.DIRTY, new ArrayList<>()));
        Pool request = this.restTemplate.postForObject("http://localhost:" + port + "/pools", pool,
                Pool.class);
        assertEquals(request.getId(), pool.getId());
        assertEquals(request.getCondition(), pool.getCondition());
        assertEquals(request.getMaxCapacity(), pool.getMaxCapacity());
        assertEquals(request.getVolume(), pool.getVolume());
        assertEquals(request.getFishes(), pool.getFishes());
    }

    @Test
    public void addFishToPool() {
        Pool pool = this.pools.get(0);
        Fish fish = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", null, pool);
        pool.addFish(fish);
        Mockito.when(poolService.save(pool)).thenReturn(new Pool(1L, 10L, 10.5, Pool.WaterCondition.CLEAN, pool.getFishes()));
        Pool request = this.restTemplate.postForObject("http://localhost:" + port + "/pools/1", fish,
                Pool.class);
        assertEquals(request.getId(), pool.getId());
        assertEquals(request.getVolume(), pool.getVolume());
        assertEquals(request.getMaxCapacity(), pool.getMaxCapacity());
        assertEquals(request.getCondition(), pool.getCondition());
        assertEquals(request.getFishes(), pool.getFishes());
    }
}
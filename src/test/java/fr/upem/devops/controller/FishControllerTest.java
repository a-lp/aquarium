package fr.upem.devops.controller;

import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.*;
import fr.upem.devops.service.FishService;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.SpecieService;
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
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FishControllerTest {
    @MockBean
    private FishService fishService;
    @MockBean
    private SpecieService specieService;
    @MockBean
    private PoolService poolService;

    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Fish> fish = new ArrayList<>();
    private List<Specie> species = new ArrayList<>();

    @Before
    public void init() {
        short lf = 1;
        Fish p1 = new Fish(1L, "Shark", FishGender.HERMAPHRODITE, "forti mascelle e di dimensioni medio-grandi", null, null);
        Fish p2 = new Fish(2L, "Codfish", FishGender.MALE, "buono da fare al forno", null, null);
        Fish p3 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", null, null);
        Specie s1 = new Specie(1L, "Specie1", lf++, lf, Alimentation.CARNIVORE, new HashSet<>(Collections.singletonList(p1)));
        Specie s2 = new Specie(2L, "Specie2", lf++, lf, Alimentation.HERBIVORE, new HashSet<>(Collections.singletonList(p2)));
        Specie s3 = new Specie(3L, "Specie3", lf++, lf, Alimentation.OMNIVORE, new HashSet<>(Collections.singletonList(p3)));
        p1.setSpecie(s1);
        p2.setSpecie(s2);
        p3.setSpecie(s3);
        fish.addAll(Arrays.asList(p1, p2, p3));
        species.addAll(Arrays.asList(s1, s2, s3));
        Mockito.when(fishService.getAll()).thenReturn(fish);
        Mockito.when(fishService.getById(1L)).thenReturn(p1);
        Mockito.when(fishService.getById(2L)).thenReturn(p2);
        Mockito.when(fishService.getById(3L)).thenReturn(p3);
        Mockito.when(specieService.getByName("Specie1")).thenReturn(s1);
        Mockito.when(specieService.getByName("Specie2")).thenReturn(s2);
        Mockito.when(specieService.getByName("Specie3")).thenReturn(s3);
    }

    @Test
    public void getAll() {
        List<Fish> lista = this.restTemplate.getForObject("http://localhost:" + port + "/fishes", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getBySpecies() {
        List<String> fishes = this.restTemplate.getForObject("http://localhost:" + port + "/species/Specie1/fishes", List.class);
        assertEquals(1, fishes.size());
        assertEquals(species.get(0).getName(), fishes.get(0));
    }

    @Test
    public void getBySpeciesNotFound() {
        Mockito.when(specieService.getByName("Specie4")).thenThrow(new ResourceNotFoundException("Species 'Specie4' not found!"));
        ResourceNotFoundException exception = this.restTemplate.getForObject("http://localhost:" + port + "/species/Specie4/fishes", ResourceNotFoundException.class);
        assertEquals("Species 'Specie4' not found!", exception.getMessage());
    }

    @Test
    public void getById() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/fishes", List.class);
        Fish output = this.restTemplate.getForObject("http://localhost:" + port + "/fishes/2", Fish.class);
        assertEquals(lista.get(1).get("name"), output.getName());
    }

    @Test
    public void addFish() {
        Pool pool = new Pool(1L, 10L, 10.5, Pool.WaterCondition.CLEAN, new HashSet<>());
        Specie specie = species.get(0);
        Fish fish = new Fish("Lesso", FishGender.HERMAPHRODITE, "buono da fare al forno con le patate", null, null);
        Fish fish_new = new Fish(4L, "Lesso", FishGender.HERMAPHRODITE, "buono da fare al forno con le patate", specie, pool);
        fish_new.setArrivalDate(new Date());
        pool.addFish(fish_new);
        specie.addFish(fish_new);
        Mockito.when(fishService.save(fish)).thenReturn(fish_new);
        Mockito.when(poolService.getById(1L)).thenReturn(pool);
        Mockito.when(specieService.getByName("Specie1")).thenReturn(specie);
        LinkedHashMap request = this.restTemplate.postForObject("http://localhost:" + port + "/species/Specie1/pools/1/fishes", fish,
                LinkedHashMap.class);
        assertEquals(fish_new.getName(), request.get("name"));
        assertEquals(fish_new.getDistinctSign(), request.get("distinctSign"));
        assertEquals(fish_new.getGender().name(), request.get("gender"));
        assertEquals(fish_new.getId().toString(), request.get("id").toString());
        assertNotNull(request.get("arrivalDate"));
        assertEquals(fish_new.getPool().getId().toString(), request.get("pool").toString());
        assertEquals(fish_new.getSpecie().getName(), request.get("specie"));
    }

    @Test
    public void addFishSpecieNotFound() {
        Mockito.when(specieService.getByName("Specie4")).thenThrow(new ResourceNotFoundException("Species 'Specie4' not found!"));
        Fish fish = new Fish("Lesso", FishGender.HERMAPHRODITE, "buono da fare al forno con le patate", null, null);
        ResourceNotFoundException request = this.restTemplate.postForObject("http://localhost:" + port + "/species/Specie4/pools/1/fishes", fish,
                ResourceNotFoundException.class);
        assertEquals("Species 'Specie4' not found!", request.getMessage());
    }

    @Test
    public void addFishPoolNotFound() {
        Mockito.when(poolService.getById(2L)).thenThrow(new ResourceNotFoundException("Pool with id '2' not found!"));
        Fish fish = new Fish("Lesso", FishGender.HERMAPHRODITE, "buono da fare al forno con le patate", null, null);
        ResourceNotFoundException request = this.restTemplate.postForObject("http://localhost:" + port + "/species/Specie1/pools/2/fishes", fish,
                ResourceNotFoundException.class);
        assertEquals("Pool with id '2' not found!", request.getMessage());
    }

    @Test
    public void retireFish() {
        Fish updateP1 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", new Specie(), new Pool());
        updateP1.setReturnDate(new Date());
        Mockito.when(fishService.save(updateP1)).thenReturn(updateP1);
        HttpEntity<Fish> updated = new HttpEntity<Fish>(updateP1);
        Fish request = this.restTemplate.exchange("http://localhost:" + port + "/fishes/retire/3", HttpMethod.PUT,
                updated, Fish.class).getBody();
        assertNotNull(request.getReturnDate());
    }

    @Test
    public void updateFish() {
        Fish updateP1 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", null, null);
        Mockito.when(fishService.save(updateP1)).thenReturn(new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", null, null));
        updateP1.setName("MauriceColdfish");
        HttpEntity<Fish> updated = new HttpEntity<>(updateP1);
        Fish request = this.restTemplate.exchange("http://localhost:" + port + "/fishes/3", HttpMethod.PUT,
                updated, Fish.class).getBody();
        assertEquals(updateP1, request);
    }

    @Test
    public void deleteFish() {
        Fish p1 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", new Specie(), new Pool());
        Mockito.when(fishService.remove(p1)).thenReturn(p1);
        Fish response = this.restTemplate
                .exchange("http://localhost:" + port + "/fishes/3", HttpMethod.DELETE, null, Fish.class)
                .getBody();
        assertEquals(Long.valueOf(3L), response.getId());
        assertEquals(p1, response);
    }
}

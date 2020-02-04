package fr.upem.devops.controller;

import fr.upem.devops.model.Fish;
import fr.upem.devops.model.FishGender;
import fr.upem.devops.model.Specie;
import fr.upem.devops.service.FishService;
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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FishControllerTest {
    @MockBean
    private FishService fishService;
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Fish> fish = new ArrayList<>();

    @Before
    public void init() {
        Fish p1 = new Fish(1L, "Shark", FishGender.HERMAPHRODITE, "forti mascelle e di dimensioni medio-grandi", new Specie());
        Fish p2 = new Fish(2L, "Codfish", FishGender.MALE, "buono da fare al forno", new Specie());
        Fish p3 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", new Specie());

        fish.add(p1);
        fish.add(p2);
        fish.add(p3);
        Mockito.when(fishService.getAll()).thenReturn(fish);
        Mockito.when(fishService.getById("1")).thenReturn(p1);
        Mockito.when(fishService.getById("2")).thenReturn(p2);
        Mockito.when(fishService.getById("3")).thenReturn(p3);
    }

    @Test
    public void getAll() {
        List<Fish> lista = this.restTemplate.getForObject("http://localhost:" + port + "/fishes", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getById() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/fishes", List.class);
        Fish output = this.restTemplate.getForObject("http://localhost:" + port + "/fishes/2", Fish.class);
        assertEquals(lista.get(1).get("name"), output.getName());
    }

    @Test
    public void addFish() throws URISyntaxException {
        Fish fish = new Fish("Lesso", FishGender.HERMAPHRODITE, "buono da fare al forno con le patate", new Specie());
        Fish fish_new = new Fish(4L, "Lesso", FishGender.HERMAPHRODITE, "buono da fare al forno con le patate", new Specie());
        fish_new.setArrivalDate(new Date());
        Mockito.when(fishService.save(fish)).thenReturn(fish_new);
        Fish request = this.restTemplate.postForObject("http://localhost:" + port + "/fishes", fish,
                Fish.class);
        assertEquals("Lesso", request.getName());
        assertEquals("buono da fare al forno con le patate", request.getDistinctSign());
        assertEquals(FishGender.HERMAPHRODITE, request.getGender());
        assertNotNull(request.getArrivalDate());
        assertNotNull(request.getId());
    }

    @Test
    public void retireFish() {
        Fish updateP1 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", new Specie());
        updateP1.setReturnDate(new Date());
        Mockito.when(fishService.save(updateP1)).thenReturn(updateP1);
        HttpEntity<Fish> updated = new HttpEntity<Fish>(updateP1);
        Fish request = this.restTemplate.exchange("http://localhost:" + port + "/fishes/retire/3", HttpMethod.PUT,
                updated, Fish.class).getBody();
        assertNotNull(request.getReturnDate());
    }

    @Test
    public void updateFish() {
        Fish updateP1 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", new Specie());
        Mockito.when(fishService.save(updateP1)).thenReturn(new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", new Specie()));
        updateP1.setName("MauriceColdfish");
        HttpEntity<Fish> updated = new HttpEntity<Fish>(updateP1);
        Fish request = this.restTemplate.exchange("http://localhost:" + port + "/fishes/3", HttpMethod.PUT,
                updated, Fish.class).getBody();
        System.out.println(request);
        assertEquals(updateP1, request);
    }

    @Test
    public void deleteFish() {
        Fish p1 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", new Specie());
        Mockito.when(fishService.remove(p1)).thenReturn(p1);
        Fish response = this.restTemplate
                .exchange("http://localhost:" + port + "/fishes/3", HttpMethod.DELETE, null, Fish.class)
                .getBody();
        assertEquals(Long.valueOf(3L), response.getId());
        assertEquals(p1, response);
    }
}
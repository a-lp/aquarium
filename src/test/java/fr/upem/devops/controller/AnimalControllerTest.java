package fr.upem.devops.controller;

import fr.upem.devops.model.Animal;
import fr.upem.devops.model.AnimalGender;
import fr.upem.devops.model.Specie;
import fr.upem.devops.service.AnimalService;
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
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnimalControllerTest {
    @MockBean
    private AnimalService animalService;
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Animal> animals = new ArrayList<>();

    @Before
    public void init() {
        Animal p1 = new Animal(1L, "Shark", AnimalGender.HERMAPHRODITE, "forti mascelle e di dimensioni medio-grandi", new Specie());
        Animal p2 = new Animal(2L, "Codfish", AnimalGender.MALE, "buono da fare al forno", new Specie());
        Animal p3 = new Animal(3L, "Swordfish", AnimalGender.FEMALE, "in padella panato", new Specie());

        animals.add(p1);
        animals.add(p2);
        animals.add(p3);
        Mockito.when(animalService.getAll()).thenReturn(animals);
        Mockito.when(animalService.getById("1")).thenReturn(p1);
        Mockito.when(animalService.getById("2")).thenReturn(p2);
        Mockito.when(animalService.getById("3")).thenReturn(p3);
    }

    @Test
    public void getAll() {
        List<Animal> lista = this.restTemplate.getForObject("http://localhost:" + port + "/animals", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getById() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/animals", List.class);
        Animal output = this.restTemplate.getForObject("http://localhost:" + port + "/animals/2", Animal.class);
        assertEquals(lista.get(1).get("name"), output.getName());
    }

    @Test
    public void addAnimal() throws URISyntaxException {
        Animal animal = new Animal("Lesso", AnimalGender.HERMAPHRODITE, "buono da fare al forno con le patate", new Specie());
        Mockito.when(animalService.save(animal)).thenReturn(new Animal(4L, "Lesso", AnimalGender.HERMAPHRODITE, "buono da fare al forno con le patate", new Specie()));
        Animal request = this.restTemplate.postForObject("http://localhost:" + port + "/animals", animal,
                Animal.class);
        assertEquals("Lesso", request.getName());
        assertEquals("buono da fare al forno con le patate", request.getDistinctSign());
        assertEquals(AnimalGender.HERMAPHRODITE, request.getGender());
        assertNotNull(request.getId());
    }

    @Test
    public void updateAnimal() {
        Animal updateP1 = new Animal(3L, "Swordfish", AnimalGender.FEMALE, "in padella panato", new Specie());
        Mockito.when(animalService.save(updateP1)).thenReturn(new Animal(3L, "Swordfish", AnimalGender.FEMALE, "in padella panato", new Specie()));
        updateP1.setName("MauriceColdfish");
        HttpEntity<Animal> updated = new HttpEntity<Animal>(updateP1);
        Animal request = this.restTemplate.exchange("http://localhost:" + port + "/animals/3", HttpMethod.PUT,
                updated, Animal.class).getBody();
        System.out.println(request);
        assertEquals(updateP1, request);
    }

    @Test
    public void deleteAnimal() {
        Animal p1 = new Animal(3L, "Swordfish", AnimalGender.FEMALE, "in padella panato", new Specie());
        Mockito.when(animalService.remove(p1)).thenReturn(p1);
        Animal response = this.restTemplate
                .exchange("http://localhost:" + port + "/animals/3", HttpMethod.DELETE, null, Animal.class)
                .getBody();
        assertEquals(Long.valueOf(3L), response.getId());
        assertEquals(p1, response);
    }
}
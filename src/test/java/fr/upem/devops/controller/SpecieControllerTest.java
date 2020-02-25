package fr.upem.devops.controller;

import fr.upem.devops.model.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpecieControllerTest {
    @MockBean
    private SpecieService specieService;
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Specie> species = new ArrayList<>();

    @Before
    public void init() {
        short lf = 1;
        Specie s1 = new Specie(1L, "Specie1", lf++, lf, Alimentation.CARNIVORE, new HashSet<>());
        Specie s2 = new Specie(2L, "Specie2", lf++, lf, Alimentation.HERBIVORE, new HashSet<>());
        Specie s3 = new Specie(3L, "Specie3", lf++, lf, Alimentation.OMNIVORE, new HashSet<>());

        Fish a1 = new Fish(1L, "Shark", FishGender.HERMAPHRODITE, "forti mascelle e di dimensioni medio-grandi", s1, null);
        Fish a2 = new Fish(2L, "Codfish", FishGender.MALE, "buono da fare al forno", s2, null);
        Fish a3 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", s3, null);

        s1.addFish(a1);
        s2.addFish(a2);
        s3.addFish(a3);

        species.add(s1);
        species.add(s2);
        species.add(s3);
        Mockito.when(specieService.getAll()).thenReturn(species);
        Mockito.when(specieService.getByName("Specie1")).thenReturn(s1);
        Mockito.when(specieService.getByName("Specie2")).thenReturn(s2);
        Mockito.when(specieService.getByName("Specie3")).thenReturn(s3);
    }

    @Test
    public void getAll() {
        List lista = this.restTemplate.getForObject("http://localhost:" + port + "/species", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getByName() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/species", List.class);
        HashMap<String, Object> output = this.restTemplate.getForObject("http://localhost:" + port + "/species/Specie1", HashMap.class);
        assertEquals(lista.get(0).get("id").toString(), output.get("id").toString());
        assertEquals(lista.get(0).get("name"), output.get("name"));
        assertEquals(lista.get(0).get("alimentation"), output.get("alimentation"));
        assertEquals(lista.get(0).get("extinctionLevel").toString(), output.get("extinctionLevel").toString());
        assertEquals(lista.get(0).get("lifeSpan").toString(), output.get("lifeSpan").toString());
        assertEquals(((List<Fish>) lista.get(0).get("fishList")).size(), ((List) output.get("fishList")).size());
    }

    @Test
    public void addSpecie() {
        short lf = 1;
        Specie specie = new Specie("Specie5", lf, lf, Alimentation.OMNIVORE, new HashSet<>());
        Mockito.when(specieService.save(specie)).thenReturn(new Specie(5L, "Specie5", lf, lf, Alimentation.OMNIVORE, new HashSet<>()));
        Specie request = this.restTemplate.postForObject("http://localhost:" + port + "/species", specie,
                Specie.class);
        assertEquals(request.getId(), Long.valueOf(5l));
        assertEquals(request.getName(), specie.getName());
        assertEquals(request.getAlimentation(), specie.getAlimentation());
        assertEquals(request.getExtinctionLevel(), specie.getExtinctionLevel());
        assertEquals(request.getLifeSpan(), specie.getLifeSpan());
        assertEquals(request.getFishList(), specie.getFishList());
    }


    @Test
    public void updateSpecie() {
        Specie specie = this.species.get(0);
        Mockito.when(specieService.save(specie)).thenReturn(specie);
        specie.setAlimentation(Alimentation.LIMNIVORE);
        specie.setLifeSpan((short) 63);
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", specie.getId().toString());
        parameters.put("name;", specie.getName());
        parameters.put("lifeSpan", specie.getLifeSpan().toString());
        parameters.put("extinctionLevel", specie.getExtinctionLevel().toString());
        parameters.put("alimentation", specie.getAlimentation().name());
        HttpEntity<HashMap> httpEntity = new HttpEntity<>(parameters);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/species/Specie1", HttpMethod.PUT, httpEntity, HashMap.class).getBody();
        assertEquals(specie.getId().toString(), request.get("id").toString());
        assertEquals(specie.getAlimentation().name(), request.get("alimentation"));
        assertEquals(specie.getLifeSpan().toString(), request.get("lifeSpan").toString());
    }

    @Test
    public void deleteSpecie() {
        Specie specie = new Specie();
        specie.setId(10L);
        specie.setName("SpecieNew");
        Mockito.when(specieService.getByName(specie.getName())).thenReturn(specie);
        Mockito.when(specieService.remove(specie)).thenReturn(specie);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/species/SpecieNew", HttpMethod.DELETE, null, HashMap.class).getBody();
        assertEquals(specie.getId().toString(), request.get("id").toString());
        assertEquals(specie.getName(), request.get("name").toString());
    }


    @Test
    public void getByIdNotFound() {
        Mockito.when(specieService.getById(10L)).thenReturn(null);
        ResponseEntity<Sector> response = this.restTemplate.getForEntity("http://localhost:" + port + "/species/id/10", Sector.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByNameNotFound() {
        Mockito.when(specieService.getByName("10")).thenReturn(null);
        ResponseEntity<Sector> response = this.restTemplate.getForEntity("http://localhost:" + port + "/species/10", Sector.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByIdBadRequest() {
        ResponseEntity<Sector> response = this.restTemplate.getForEntity("http://localhost:" + port + "/species/id/asdf", Sector.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void addExistentSpecie() {
        Specie specie = this.species.get(0);
        specie.setFishList(new HashSet<>());
        Mockito.when(specieService.getByName(specie.getName())).thenReturn(specie);
        ResponseEntity<HashMap> response = this.restTemplate.postForEntity("http://localhost:" + port + "/species", specie,
                HashMap.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Another specie named '" + specie.getName() + "' found!", response.getBody().get("message"));
    }
}

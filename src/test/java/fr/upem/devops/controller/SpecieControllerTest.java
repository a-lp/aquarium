package fr.upem.devops.controller;

import fr.upem.devops.errors.ConflictException;
import fr.upem.devops.model.Alimentation;
import fr.upem.devops.model.Fish;
import fr.upem.devops.model.FishGender;
import fr.upem.devops.model.Specie;
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

import java.util.ArrayList;
import java.util.HashMap;
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
        Specie s1 = new Specie(1L, "Specie1", lf++, lf, Alimentation.CARNIVORE, new ArrayList<>());
        Specie s2 = new Specie(2L, "Specie2", lf++, lf, Alimentation.HERBIVORE, new ArrayList<>());
        Specie s3 = new Specie(3L, "Specie3", lf++, lf, Alimentation.OMNIVORE, new ArrayList<>());

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
        Specie output = this.restTemplate.getForObject("http://localhost:" + port + "/species/Specie1", Specie.class);
        assertEquals(lista.get(0).get("id").toString(), output.getId().toString());
        assertEquals(lista.get(0).get("name"), output.getName());
        assertEquals(lista.get(0).get("alimentation"), output.getAlimentation().name());
        assertEquals(lista.get(0).get("extinctionLevel").toString(), output.getExtinctionLevel().toString());
        assertEquals(lista.get(0).get("lifeSpan").toString(), output.getLifeSpan().toString());
        assertEquals(((List<Fish>) lista.get(0).get("fishList")).size(), output.getFishList().size());
    }

    @Test
    public void addSpecie() {
        short lf = 1;
        Specie specie = new Specie("Specie5", lf, lf, Alimentation.OMNIVORE, new ArrayList<>());
        Mockito.when(specieService.save(specie)).thenReturn(new Specie(5L, "Specie5", lf, lf, Alimentation.OMNIVORE, new ArrayList<>()));
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
    public void addExistentSpecie() {
        short lf = 1;
        Specie specie = new Specie(4L, "Specie3", lf++, lf, Alimentation.OMNIVORE, new ArrayList<>());
        ConflictException conflictError = new ConflictException("Another specie named '" + specie.getName() + "' found!");
        Mockito.when(specieService.save(specie)).thenThrow(conflictError);
        ConflictException request = this.restTemplate.postForObject("http://localhost:" + port + "/species", specie,
                ConflictException.class);
        assertEquals(conflictError.getMessage(), request.getMessage());
    }

    @Test
    public void updateSpecie() {
        Specie specie = this.species.get(0);
        Mockito.when(specieService.save(specie)).thenReturn(specie);
        specie.addFish(new Fish());
        HttpEntity<Specie> httpEntity = new HttpEntity<>(specie);
        Specie request = this.restTemplate.exchange("http://localhost:" + port + "/species/Specie1", HttpMethod.PUT, httpEntity, Specie.class).getBody();
        assertEquals(specie, request);
        assertEquals(specie.getFishList(), request.getFishList());
    }

    @Test
    public void deleteSpecie() {
        Specie specie = this.species.get(0);
        Mockito.when(specieService.remove(specie)).thenReturn(specie);
        Specie request = this.restTemplate.exchange("http://localhost:" + port + "/species/Specie1", HttpMethod.DELETE, null, Specie.class).getBody();
        assertEquals(specie, request);
    }
}
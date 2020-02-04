package fr.upem.devops.controller;

import fr.upem.devops.model.Alimentation;
import fr.upem.devops.model.Fish;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

//        Fish a1 = new Fish(1L, "Shark", FishGender.HERMAPHRODITE, "forti mascelle e di dimensioni medio-grandi", s1);
//        Fish a2 = new Fish(2L, "Codfish", FishGender.MALE, "buono da fare al forno", s2);
//        Fish a3 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", s3);
//
//        s1.addAnimal(a1);
//        s2.addAnimal(a2);
//        s3.addAnimal(a3);

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
        Fish output = this.restTemplate.getForObject("http://localhost:" + port + "/species/Specie1", Fish.class);
        assertEquals(lista.get(0).get("name"), output.getName());
    }

    @Test
    public void addSpecie() {
        short lf = 1;
        Specie specie = new Specie("Specie5", lf, lf, Alimentation.OMNIVORE, new ArrayList<>());
        Mockito.when(specieService.save(specie)).thenReturn(new Specie(5L, "Specie5", lf, lf, Alimentation.OMNIVORE, new ArrayList<>()));
        Specie request = this.restTemplate.postForObject("http://localhost:" + port + "/species", specie,
                Specie.class);
        assertEquals(request.getName(), specie.getName());
    }

    @Test
    public void addExistentSpecie() {
        short lf = 1;
        Specie specie = new Specie(4L, "Specie3", lf++, lf, Alimentation.OMNIVORE, new ArrayList<>());
        Mockito.when(specieService.save(specie)).thenReturn(null);
        Specie request = this.restTemplate.postForObject("http://localhost:" + port + "/species", specie,
                Specie.class);
        assertNull(request);
    }
}
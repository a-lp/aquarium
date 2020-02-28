package fr.upem.devops.controller;

import fr.upem.devops.model.*;
import fr.upem.devops.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FishControllerTest {
    private Pool pool;
    private User user;
    private String token = "tokenTest";
    private Staff profile;
    private HashMap<String, Object> tokenParameters = new HashMap<>();
    private HttpHeaders headers = new HttpHeaders();
    @MockBean
    private JWTService jwtService;
    @MockBean
    private JWTAuthenticationService authenticationService;
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
        this.pool = new Pool(1L, 10L, 10.5, Pool.WaterCondition.CLEAN, new HashSet<>());
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
        Mockito.when(poolService.getById(1L)).thenReturn(pool);
        /* Spring Security */
        this.user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        this.profile = new Staff();
        this.profile.setCredentials(this.user);
        this.profile.setId(100L);
        this.profile.setRole(Staff.StaffRole.ADMIN);
        this.user.setProfile(this.profile);
        this.tokenParameters.put("iat", new Date().getTime());
        this.tokenParameters.put("exp", Date.from(Instant.now().plusSeconds(60 * 5000)).getTime());
        this.tokenParameters.put("username", this.user.getUsername());
        this.tokenParameters.put("id", this.user.getProfile().getId());
        this.tokenParameters.put("role", this.user.getProfile().getRole().name());
        this.headers.add("Authorization", "Bearer " + this.token);
        Mockito.when(jwtService.create(this.user.getUsername(), this.user.getProfile())).thenReturn(this.token);
        Mockito.when(jwtService.verify(this.token)).thenReturn(this.tokenParameters);
        Mockito.when(authenticationService.authenticateByToken("tokenTest")).thenReturn(this.user);
    }

    @Test
    public void getAll() {
        List<Fish> lista = this.restTemplate.getForObject("http://localhost:" + port + "/api/fishes", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getBySpecies() {
        List<HashMap> fishes = this.restTemplate.getForObject("http://localhost:" + port + "/api/species/Specie1/fishes", List.class);
        assertEquals(1, fishes.size());
        assertEquals(species.get(0).getName(), fishes.get(0).get("specie"));
    }

    @Test
    public void getById() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/api/fishes", List.class);
        HashMap output = this.restTemplate.getForObject("http://localhost:" + port + "/api/fishes/2", HashMap.class);
        assertEquals(lista.get(1).get("name"), output.get("name"));
    }

    @Test
    public void addFish() {
        Specie specie = species.get(0);
        Fish fish = new Fish("Lesso", FishGender.HERMAPHRODITE, "buono da fare al forno con le patate", null, null);
        Fish fish_new = new Fish(4L, "Lesso", FishGender.HERMAPHRODITE, "buono da fare al forno con le patate", specie, pool);
        fish_new.setArrivalDate(new Date());
        pool.addFish(fish_new);
        specie.addFish(fish_new);
        Mockito.when(fishService.save(fish)).thenReturn(fish_new);
        HttpEntity<Fish> httpEntity = new HttpEntity(fish, this.headers);
        LinkedHashMap request = this.restTemplate.postForObject("http://localhost:" + port + "/api/species/Specie1/pools/1/fishes", httpEntity,
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
    public void retireFish() {
        Fish updateP1 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", this.species.get(0), this.pool);
        updateP1.setReturnDate(new Date());
        Mockito.when(fishService.save(updateP1)).thenReturn(updateP1);
        HttpEntity<Fish> httpEntity = new HttpEntity(updateP1, this.headers);
        HashMap<String, String> request = this.restTemplate.exchange("http://localhost:" + port + "/api/fishes/retire/3", HttpMethod.PUT,
                httpEntity, HashMap.class).getBody();
        assertNotNull(request.containsKey("returnDate"));
    }

    @Test
    public void updateFish() {
        Fish updateP1 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", this.species.get(0), new Pool(1L, 1l, 1.0, Pool.WaterCondition.DIRTY, null));
        Mockito.when(fishService.save(updateP1)).thenReturn(new Fish(3L, "MauriceColdfish", FishGender.FEMALE, "in padella panato", null, null));
        updateP1.setName("MauriceColdfish");
        HttpEntity<Fish> updated = new HttpEntity<>(updateP1, this.headers);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/api/fishes/3", HttpMethod.PUT,
                updated, HashMap.class).getBody();
        assertEquals(updateP1.getId().toString(), request.get("id").toString());
        assertEquals(updateP1.getName(), request.get("name").toString());
    }

    @Test
    public void deleteFish() {
        Fish p1 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", this.species.get(0), pool);
        Mockito.when(fishService.remove(p1)).thenReturn(p1);
        HttpEntity<Fish> httpEntity = new HttpEntity(null, this.headers);
        HashMap<String, Object> response = this.restTemplate
                .exchange("http://localhost:" + port + "/api/fishes/3", HttpMethod.DELETE, httpEntity, HashMap.class)
                .getBody();
        assertEquals("3", response.get("id").toString());
    }

    /* HTTP ERRORS */

    @Test
    public void getBySpeciesNotFound() {
        Mockito.when(specieService.getByName("Specie4")).thenReturn(null);
        ResponseEntity<Fish> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/species/Specie4/fishes", Fish.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addFishSpecieNotFound() {
        Mockito.when(specieService.getByName("Specie4")).thenReturn(null);
        Fish fish = new Fish("Lesso", FishGender.HERMAPHRODITE, "buono da fare al forno con le patate", null, null);
        HttpEntity<Fish> httpEntity = new HttpEntity(fish, this.headers);
        ResponseEntity<Fish> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/species/Specie4/pools/2/fishes", httpEntity,
                Fish.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addFishPoolNotFound() {
        Mockito.when(poolService.getById(2L)).thenReturn(null);
        Fish fish = new Fish("Lesso", FishGender.HERMAPHRODITE, "buono da fare al forno con le patate", null, null);
        HttpEntity<Fish> httpEntity = new HttpEntity(fish, this.headers);
        ResponseEntity<Fish> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/species/Specie1/pools/2/fishes", httpEntity,
                Fish.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByIdNotFound() {
        Mockito.when(fishService.getById(10L)).thenReturn(null);
        ResponseEntity<Fish> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/fishes/10", Fish.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByIdBadRequest() {
        ResponseEntity<Fish> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/fishes/asdf", Fish.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateFishBadRequest() {
        Fish updateP1 = new Fish(3L, "Swordfish", FishGender.FEMALE, "in padella panato", this.species.get(0), new Pool(1L, 1L, 1.0, Pool.WaterCondition.DIRTY, null));
        Mockito.when(fishService.save(updateP1)).thenReturn(null);
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("gender", "wow");
        HttpEntity<HashMap> updated = new HttpEntity<>(parameters, this.headers);
        ResponseEntity<Fish> response = this.restTemplate.exchange("http://localhost:" + port + "/api/fishes/3", HttpMethod.PUT,
                updated, Fish.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}



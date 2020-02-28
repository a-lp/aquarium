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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PoolControllerTest {
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
        Set Seta = this.restTemplate.getForObject("http://localhost:" + port + "/api/pools", Set.class);
        assertEquals(3, Seta.size());
    }

    @Test
    public void getById() {
        List<HashMap> poolSet = this.restTemplate.getForObject("http://localhost:" + port + "/api/pools", List.class);
        HashMap<String, Object> request = this.restTemplate.getForObject("http://localhost:" + port + "/api/pools/1", HashMap.class);
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
        HttpEntity<Pool> httpEntity = new HttpEntity<>(pool, this.headers);
        HashMap<String, Object> request = this.restTemplate.postForObject("http://localhost:" + port + "/api/sectors/1/responsible/1/pools", httpEntity,
                HashMap.class);
        assertEquals(pool.getId().toString(), request.get("id").toString());
        assertEquals(pool.getCondition().name(), request.get("condition"));
        assertEquals(pool.getMaxCapacity().toString(), request.get("maxCapacity").toString());
        assertEquals(pool.getVolume().toString(), request.get("volume").toString());
        assertEquals(pool.getFishes().size(), ((List) request.get("fishes")).size());
        assertEquals(pool_new.getSector().getName(), request.get("sector"));
    }

    @Test
    public void updatePool() {
        Pool pool = this.pools.get(0);
        Mockito.when(poolService.save(pool)).thenReturn(pool);
        pool.setCondition(Pool.WaterCondition.DIRTY);
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("maxCapacity", pool.getMaxCapacity().toString());
        parameters.put("volume", pool.getVolume().toString());
        parameters.put("condition", pool.getCondition().name());
        HttpEntity<HashMap> httpEntity = new HttpEntity<>(parameters, this.headers);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/api/pools/1", HttpMethod.PUT, httpEntity, HashMap.class).getBody();
        assertEquals(pool.getId().toString(), request.get("id").toString());
        assertEquals(pool.getCondition().name(), request.get("condition"));
    }

    @Test
    public void deletePool() {
        Pool pool = new Pool(4L, 30L, 30.5, Pool.WaterCondition.DIRTY, new HashSet<>());
        Mockito.when(poolService.getById(4L)).thenReturn(pool);
        Mockito.when(poolService.remove(pool)).thenReturn(pool);
        HttpEntity<Pool> httpEntity = new HttpEntity<>(null, this.headers);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/api/pools/4", HttpMethod.DELETE, httpEntity, HashMap.class).getBody();
        assertEquals(pool.getId().toString(), request.get("id").toString());
    }

    /* HTTP EXCEPTIONS */

    @Test
    public void getByIdNotFound() {
        Mockito.when(poolService.getById(10L)).thenReturn(null);
        ResponseEntity<Pool> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/schedules/10", Pool.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByIdBadRequest() {
        ResponseEntity<Pool> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/schedules/asdf", Pool.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void addPoolSectorNotFound() {
        Mockito.when(sectorService.getById(2L)).thenReturn(null);
        HttpEntity<Pool> httpEntity = new HttpEntity<>(new Pool(), this.headers);
        ResponseEntity<Pool> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/sectors/2/responsible/2/pools", httpEntity,
                Pool.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addPoolResponsibleNotFound() {
        Mockito.when(sectorService.getById(1L)).thenReturn(this.sectors.get(0));
        Mockito.when(staffService.getById(2L)).thenReturn(null);
        HttpEntity<Pool> httpEntity = new HttpEntity<>(new Pool(), this.headers);
        ResponseEntity<Pool> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/sectors/1/responsible/2/pools", httpEntity,
                Pool.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateBadRequest() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("maxCapacity", "wow");
        HttpEntity<HashMap> updated = new HttpEntity<>(parameters, this.headers);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/api/pools/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("maxCapacity value '" + parameters.get("maxCapacity") + "' cannot be converted in Long!", response.getBody().get("message"));
        parameters.remove("maxCapacity");
        parameters.put("condition", "a,2");
        updated = new HttpEntity<>(parameters, this.headers);
        response = this.restTemplate.exchange("http://localhost:" + port + "/api/pools/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("condition value '" + parameters.get("condition") + "' cannot be converted in WaterCondition!", response.getBody().get("message"));
    }

    @Test
    public void deleteBadRequest() {
        HttpEntity<HashMap> httpEntity = new HttpEntity<>(null, this.headers);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/api/pools/1", HttpMethod.DELETE, httpEntity, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Impossible to delete the pool! Fishes assigned to pool '1' must be moved to another pool before removing!", response.getBody().get("message"));
    }
}

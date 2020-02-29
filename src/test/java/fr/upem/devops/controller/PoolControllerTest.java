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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PoolControllerTest {
    private User userAdmin;
    private String tokenAdmin = "tokenAdminTest";
    private Staff profileAdmin;
    private HttpHeaders headersAdmin = new HttpHeaders();
    private HashMap<String, Object> tokenAdminParameters = new HashMap<>();

    private User userManager;
    private String tokenManager = "tokenManagerTest";
    private Staff profileManager;
    private HttpHeaders headersManager = new HttpHeaders();
    private HashMap<String, Object> tokenManagerParameters = new HashMap<>();

    private User userWorker;
    private String tokenWorker = "tokenWorkerTest";
    private Staff profileWorker;
    private HttpHeaders headersWorker = new HttpHeaders();
    private HashMap<String, Object> tokenWorkerParameters = new HashMap<>();
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
        p3.setResponsible(this.profileManager);
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
        userAdmin = new User();
        userAdmin.setUsername("testUsername");
        userAdmin.setPassword("testPassword");
        profileAdmin = new Staff();
        profileAdmin.setCredentials(userAdmin);
        profileAdmin.setId(100L);
        profileAdmin.setRole(Staff.StaffRole.ADMIN);
        userAdmin.setProfile(profileAdmin);
        tokenAdminParameters.put("iat", new Date().getTime());
        tokenAdminParameters.put("exp", Date.from(Instant.now().plusSeconds(60 * 5000)).getTime());
        tokenAdminParameters.put("username", userAdmin.getUsername());
        tokenAdminParameters.put("id", userAdmin.getProfile().getId());
        tokenAdminParameters.put("role", userAdmin.getProfile().getRole().name());
        headersAdmin.add("Authorization", "Bearer " + tokenAdmin);
        Mockito.when(jwtService.create(userAdmin.getUsername(), userAdmin.getProfile())).thenReturn(tokenAdmin);
        Mockito.when(jwtService.verify(tokenAdmin)).thenReturn(tokenAdminParameters);
        Mockito.when(authenticationService.authenticateByToken(tokenAdmin)).thenReturn(userAdmin);

        userManager = new User();
        userManager.setUsername("userManager");
        userManager.setPassword("userManager");
        profileManager = new Staff();
        profileManager.setCredentials(userManager);
        profileManager.setId(101L);
        profileManager.setRole(Staff.StaffRole.MANAGER);
        userManager.setProfile(profileManager);
        tokenManagerParameters = new HashMap<>();
        tokenManagerParameters.put("iat", new Date().getTime());
        tokenManagerParameters.put("exp", Date.from(Instant.now().plusSeconds(60 * 5000)).getTime());
        tokenManagerParameters.put("username", userManager.getUsername());
        tokenManagerParameters.put("id", userManager.getProfile().getId());
        tokenManagerParameters.put("role", userManager.getProfile().getRole().name());
        headersManager.add("Authorization", "Bearer " + tokenManager);
        Mockito.when(jwtService.create(userManager.getUsername(), userManager.getProfile())).thenReturn(tokenManager);
        Mockito.when(jwtService.verify(tokenManager)).thenReturn(tokenManagerParameters);
        Mockito.when(authenticationService.authenticateByToken(tokenManager)).thenReturn(userManager);

        userWorker = new User();
        userWorker.setUsername("userWorker");
        userWorker.setPassword("userWorker");
        profileWorker = new Staff();
        profileWorker.setCredentials(userWorker);
        profileWorker.setId(102L);
        profileWorker.setRole(Staff.StaffRole.WORKER);
        userWorker.setProfile(profileWorker);
        tokenWorkerParameters = new HashMap<>();
        tokenWorkerParameters.put("iat", new Date().getTime());
        tokenWorkerParameters.put("exp", Date.from(Instant.now().plusSeconds(60 * 5000)).getTime());
        tokenWorkerParameters.put("username", userWorker.getUsername());
        tokenWorkerParameters.put("id", userWorker.getProfile().getId());
        tokenWorkerParameters.put("role", userWorker.getProfile().getRole().name());
        headersWorker.add("Authorization", "Bearer " + tokenWorker);
        Mockito.when(jwtService.create(userWorker.getUsername(), userWorker.getProfile())).thenReturn(tokenWorker);
        Mockito.when(jwtService.verify(tokenWorker)).thenReturn(tokenWorkerParameters);
        Mockito.when(authenticationService.authenticateByToken(tokenWorker)).thenReturn(userWorker);
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
        HttpEntity<Pool> httpEntity = new HttpEntity<>(pool, this.headersAdmin);
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
        HttpEntity<HashMap> httpEntity = new HttpEntity<>(parameters, this.headersAdmin);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/api/pools/1", HttpMethod.PUT, httpEntity, HashMap.class).getBody();
        assertEquals(pool.getId().toString(), request.get("id").toString());
        assertEquals(pool.getCondition().name(), request.get("condition"));
    }

    @Test
    public void deletePool() {
        Pool pool = new Pool(4L, 30L, 30.5, Pool.WaterCondition.DIRTY, new HashSet<>());
        Mockito.when(poolService.getById(4L)).thenReturn(pool);
        Mockito.when(poolService.remove(pool)).thenReturn(pool);
        HttpEntity<Pool> httpEntity = new HttpEntity<>(null, this.headersAdmin);
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
        HttpEntity<Pool> httpEntity = new HttpEntity<>(new Pool(), this.headersAdmin);
        ResponseEntity<Pool> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/sectors/2/responsible/2/pools", httpEntity,
                Pool.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addPoolResponsibleNotFound() {
        Mockito.when(sectorService.getById(1L)).thenReturn(this.sectors.get(0));
        Mockito.when(staffService.getById(2L)).thenReturn(null);
        HttpEntity<Pool> httpEntity = new HttpEntity<>(new Pool(), this.headersAdmin);
        ResponseEntity<Pool> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/sectors/1/responsible/2/pools", httpEntity,
                Pool.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateBadRequest() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("maxCapacity", "wow");
        HttpEntity<HashMap> updated = new HttpEntity<>(parameters, this.headersAdmin);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/api/pools/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("maxCapacity value '" + parameters.get("maxCapacity") + "' cannot be converted in Long!", response.getBody().get("message"));
        parameters.remove("maxCapacity");
        parameters.put("condition", "a,2");
        updated = new HttpEntity<>(parameters, this.headersAdmin);
        response = this.restTemplate.exchange("http://localhost:" + port + "/api/pools/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("condition value '" + parameters.get("condition") + "' cannot be converted in WaterCondition!", response.getBody().get("message"));
    }

    @Test
    public void deleteBadRequest() {
        HttpEntity<HashMap> httpEntity = new HttpEntity<>(null, this.headersAdmin);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/api/pools/1", HttpMethod.DELETE, httpEntity, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Impossible to delete the pool! Fishes assigned to pool '1' must be moved to another pool before removing!", response.getBody().get("message"));
    }

    @Test
    public void invalidTokenPost() {
        Mockito.when(authenticationService.authenticateByToken("not.a.good.token")).thenThrow(BadCredentialsException.class);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer not.a.good.token");
        HttpEntity<Pool> httpEntity = new HttpEntity<>(new Pool(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/sectors/1/responsible/1/pools", httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void invalidTokenPut() {
        Mockito.when(authenticationService.authenticateByToken("not.a.good.token")).thenThrow(BadCredentialsException.class);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer not.a.good.token");
        HttpEntity<Pool> httpEntity = new HttpEntity<>(new Pool(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate
                .exchange("http://localhost:" + port + "/api/pools/3", HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void invalidTokenDelete() {
        Mockito.when(authenticationService.authenticateByToken("not.a.good.token")).thenThrow(BadCredentialsException.class);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer not.a.good.token");
        HttpEntity<Pool> httpEntity = new HttpEntity<>(new Pool(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate
                .exchange("http://localhost:" + port + "/api/pools/3", HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void invalidRoleAdd() {
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer " + this.tokenManager);
        HttpEntity<Pool> httpEntity = new HttpEntity<>(new Pool(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/sectors/1/responsible/1/pools", httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void invalidRoleDelete() {
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer " + this.tokenManager);
        HttpEntity<Pool> httpEntity = new HttpEntity<>(new Pool(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate
                .exchange("http://localhost:" + port + "/api/pools/3", HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void invalidRolePut() {
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer " + this.tokenWorker);
        HttpEntity<HashMap> httpEntity = new HttpEntity<>(new HashMap<>(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate
                .exchange("http://localhost:" + port + "/api/pools/3", HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void invalidUpdateNotOwningPoolManager() {
        Pool pool = new Pool(4L, 10L, 10.5, Pool.WaterCondition.CLEAN, new HashSet<>());
        pool.setResponsible(this.profileManager);
        Mockito.when(poolService.getById(4L)).thenReturn(pool);
        tokenManagerParameters.put("id", userManager.getProfile().getId() + 1);
        System.out.println(tokenManagerParameters);
        Mockito.when(jwtService.verify(tokenManager)).thenReturn(tokenManagerParameters);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer " + tokenManager);
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("maxCapacity", pool.getMaxCapacity().toString());
        HttpEntity<HashMap> httpEntity = new HttpEntity<>(parameters, corruptedHeader);
        ResponseEntity<String> response = this.restTemplate
                .exchange("http://localhost:" + port + "/api/pools/4", HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}

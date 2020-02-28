package fr.upem.devops.controller;

import fr.upem.devops.model.*;
import fr.upem.devops.service.JWTAuthenticationService;
import fr.upem.devops.service.JWTService;
import fr.upem.devops.service.SectorService;
import fr.upem.devops.service.StaffService;
import org.assertj.core.util.Sets;
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
public class SectorControllerTest {
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
    private SectorService sectorService;

    @MockBean
    private StaffService staffService;

    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Sector> sectors = new ArrayList<>();
    private List<Staff> staff = new ArrayList<>();

    @Before
    public void init() {
        Sector s1 = new Sector(1L, "Sector1", "Location1");
        Sector s2 = new Sector(2L, "Sector2", "Location2");
        Sector s3 = new Sector(3L, "Sector3", "Location3");
        Staff st1 = new Staff(1L, "Nome1", "Cognome1", "Address1", new Date(), "SocSec1", Staff.StaffRole.ADMIN);
        Staff st2 = new Staff(2L, "Nome2", "Cognome2", "Address2", new Date(), "SocSec2", Staff.StaffRole.MANAGER);
        Staff st3 = new Staff(3L, "Nome3", "Cognome3", "Address3", new Date(), "SocSec3", Staff.StaffRole.WORKER);
        Pool a1 = new Pool(1L, 1L, 1.0, Pool.WaterCondition.DIRTY, new HashSet<>());
        Pool a2 = new Pool(2L, 2L, 2.0, Pool.WaterCondition.CLEAN, new HashSet<>());
        Pool a3 = new Pool(3L, 3L, 3.0, Pool.WaterCondition.DIRTY, new HashSet<>());

        s1.addPool(a1);
        s2.addPool(a2);
        s3.addPool(a3);

        sectors.addAll(Arrays.asList(s1, s2, s3));
        staff.addAll(Arrays.asList(st1, st2, st3));

        Mockito.when(sectorService.getAll()).thenReturn(sectors);
        Mockito.when(sectorService.getByName("Sector1")).thenReturn(s1);
        Mockito.when(sectorService.getByName("Sector2")).thenReturn(s2);

        Mockito.when(staffService.getById(1L)).thenReturn(st1);
        Mockito.when(staffService.getById(2L)).thenReturn(st2);
        Mockito.when(staffService.getById(3L)).thenReturn(st3);
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
        List lista = this.restTemplate.getForObject("http://localhost:" + port + "/api/sectors", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getByName() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/api/sectors", List.class);
        HashMap<String, Object> request = this.restTemplate.getForObject("http://localhost:" + port + "/api/sectors/Sector1", HashMap.class);
        assertEquals(lista.get(0).get("id").toString(), request.get("id").toString());
        assertEquals(lista.get(0).get("name"), request.get("name").toString());
        assertEquals(lista.get(0).get("location"), request.get("location").toString());
    }

    @Test
    public void addSector() {
        Sector sec = new Sector("Sector4", "Location4");
        Sector sec_new = new Sector(4L, "Sector4", "Location4");
        sec_new.setStaffList(Sets.newHashSet(this.staff));
        Mockito.when(sectorService.save(sec)).thenReturn(sec_new);
        HttpEntity<Sector> httpEntity = new HttpEntity<>(sec, this.headers);
        HashMap<String, Object> request = this.restTemplate.postForObject("http://localhost:" + port + "/api/sectors/responsible/1,2,3", httpEntity,
                HashMap.class);
        assertEquals(sec_new.getId().toString(), request.get("id").toString());
        assertEquals(sec_new.getName(), request.get("name").toString());
        assertEquals(sec_new.getLocation(), request.get("location").toString());
        assertEquals(sec_new.getPools().size(), ((List) request.get("pools")).size());
        assertEquals(sec_new.getStaffList().size(), ((List) request.get("staffList")).size());
    }

    @Test
    public void updateSector() {
        Sector sector = this.sectors.get(0);
        Mockito.when(sectorService.getById(1L)).thenReturn(sector);
        Mockito.when(sectorService.save(sector)).thenReturn(sector);
        sector.setName("New Name");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", sector.getName());
        parameters.put("location", sector.getLocation());
        HttpEntity<HashMap> httpEntity = new HttpEntity<>(parameters, this.headers);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/api/sectors/1", HttpMethod.PUT, httpEntity, HashMap.class).getBody();
        assertEquals(sector.getId().toString(), request.get("id").toString());
        assertEquals(sector.getName(), request.get("name"));
    }

    @Test
    public void deleteSector() {
        Sector sector = new Sector();
        sector.setId(100L);
        Mockito.when(sectorService.getByName("SectorNew")).thenReturn(sector);
        Mockito.when(sectorService.remove(sector)).thenReturn(sector);
        HttpEntity<Sector> httpEntity = new HttpEntity<>(null, this.headers);
        HashMap<String, Object> request = this.restTemplate.exchange("http://localhost:" + port + "/api/sectors/SectorNew", HttpMethod.DELETE, httpEntity, HashMap.class).getBody();
        assertEquals(sector.getId().toString(), request.get("id").toString());
    }


    @Test
    public void getByIdNotFound() {
        Mockito.when(sectorService.getById(10L)).thenReturn(null);
        ResponseEntity<Sector> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/sectors/id/10", Sector.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByNameNotFound() {
        Mockito.when(sectorService.getByName("10")).thenReturn(null);
        ResponseEntity<Sector> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/sectors/10", Sector.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getByIdBadRequest() {
        ResponseEntity<Sector> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/sectors/id/asdf", Sector.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void addSectorResponsibleNotFound() {
        Mockito.when(staffService.getById(4L)).thenReturn(null);
        HttpEntity<Sector> httpEntity = new HttpEntity<>(new Sector(), this.headers);
        ResponseEntity<Sector> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/sectors/responsible/4", httpEntity,
                Sector.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addSectorDuplicatedName() {
        Sector sector = this.sectors.get(0);
        sector.setPools(null);
        Mockito.when(sectorService.getByName(sector.getName())).thenReturn(sector);
        HttpEntity<Sector> httpEntity = new HttpEntity<>(sector, this.headers);
        ResponseEntity<HashMap> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/sectors/responsible/1", httpEntity,
                HashMap.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Duplicate name for sector '" + sector.getName(), response.getBody().get("message"));
    }

    @Test
    public void updateBadRequest() {
        Mockito.when(sectorService.getById(1L)).thenReturn(this.sectors.get(0));
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("staffList", "a,2");
        HttpEntity<HashMap> updated = new HttpEntity<>(parameters, this.headers);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/api/sectors/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error during conversion of staff ids in Long!", response.getBody().get("message"));
        parameters.put("staffList", "");
        updated = new HttpEntity<>(parameters, this.headers);
        response = this.restTemplate.exchange("http://localhost:" + port + "/api/sectors/1", HttpMethod.PUT,
                updated, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cannot update sector, no staff found! There must be at least one responsible", response.getBody().get("message"));
    }

    @Test
    public void deleteSectorBadRequest() {
        HttpEntity<Sector> httpEntity = new HttpEntity<>(null, this.headers);
        ResponseEntity<HashMap> response = this.restTemplate.exchange("http://localhost:" + port + "/api/sectors/Sector1", HttpMethod.DELETE, httpEntity, HashMap.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cannot delete sector! There are pools in this sector.", response.getBody().get("message"));
    }

    @Test
    public void invalidTokenPost() {
        Mockito.when(authenticationService.authenticateByToken("not.a.good.token")).thenThrow(BadCredentialsException.class);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer not.a.good.token");
        HttpEntity<Sector> httpEntity = new HttpEntity<>(new Sector(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port +  "/api/sectors/responsible/1", httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    @Test
    public void invalidTokenPut() {
        Mockito.when(authenticationService.authenticateByToken("not.a.good.token")).thenThrow(BadCredentialsException.class);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer not.a.good.token");
        HttpEntity<Sector> httpEntity = new HttpEntity<>(new Sector(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate
                .exchange("http://localhost:" + port + "/api/sectors/3", HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    @Test
    public void invalidTokenDelete() {
        Mockito.when(authenticationService.authenticateByToken("not.a.good.token")).thenThrow(BadCredentialsException.class);
        HttpHeaders corruptedHeader = new HttpHeaders();
        corruptedHeader.add("Authorization", "Bearer not.a.good.token");
        HttpEntity<Sector> httpEntity = new HttpEntity<>(new Sector(), corruptedHeader);
        ResponseEntity<String> response = this.restTemplate
                .exchange("http://localhost:" + port + "/api/sectors/3", HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}


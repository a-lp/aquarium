package fr.upem.devops.controller;

import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Sector;
import fr.upem.devops.service.SectorService;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectorControllerTest {
    @MockBean
    private SectorService sectorService;
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<Sector> pools = new ArrayList<>();

    @Before
    public void init() {
        short lf = 1;
        Sector s1 = new Sector(1L, "Sector1", "Location1");
        Sector s2 = new Sector(2L, "Sector2", "Location2");
        Sector s3 = new Sector(3L, "Sector3", "Location3");

        Pool a1 = new Pool(1L, 1L, 1.0, Pool.WaterCondition.DIRTY, null);
        Pool a2 = new Pool(2L, 2L, 2.0, Pool.WaterCondition.CLEAN, null);
        Pool a3 = new Pool(3L, 3L, 3.0, Pool.WaterCondition.DIRTY, null);

        s1.addPool(a1);
        s2.addPool(a2);
        s3.addPool(a3);

        pools.add(s1);
        pools.add(s2);
        pools.add(s3);
        Mockito.when(sectorService.getAll()).thenReturn(pools);
        Mockito.when(sectorService.getByName("Sector1")).thenReturn(s1);
        Mockito.when(sectorService.getByName("Sector2")).thenReturn(s2);
        Mockito.when(sectorService.getByName("Sector3")).thenReturn(s3);
    }

    @Test
    public void getAll() {
        List lista = this.restTemplate.getForObject("http://localhost:" + port + "/sectors", List.class);
        assertEquals(3, lista.size());
    }

    @Test
    public void getByName() {
        List<HashMap> lista = this.restTemplate.getForObject("http://localhost:" + port + "/sectors", List.class);
        Sector output = this.restTemplate.getForObject("http://localhost:" + port + "/sectors/Sector1", Sector.class);
        assertEquals(lista.get(0).get("id").toString(), output.getId().toString());
        assertEquals(lista.get(0).get("name"), output.getName());
        assertEquals(lista.get(0).get("location"), output.getLocation());
        assertEquals(((List<Pool>) lista.get(0).get("pools")).size(), output.getPools().size());
    }

    @Test
    public void addSector() {
        Sector sec = new Sector(4L, "Sector4", "Location4");
        Mockito.when(sectorService.save(sec)).thenReturn(new Sector(4L, "Sector4", "Location4"));
        Sector request = this.restTemplate.postForObject("http://localhost:" + port + "/sectors", sec,
                Sector.class);
        assertEquals(request.getId(), sec.getId());
        assertEquals(request.getName(), sec.getName());
        assertEquals(request.getLocation(), sec.getLocation());
        assertEquals(request.getPools(), sec.getPools());
    }
}

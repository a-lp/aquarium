package fr.upem.devops.controller;

import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Sector;
import fr.upem.devops.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SectorController {
    @Autowired
    private SectorService sectorService;

    @GetMapping("/sectors")
    public Iterable<Sector> getAll() {
        return sectorService.getAll();
    }

    @GetMapping("/sectors/{name}")
    public Sector getByName(@PathVariable String name) {
        return sectorService.getByName(name);
    }

    @PostMapping("/sectors")
    @ResponseBody
    public Sector addSector(@RequestBody Sector sector) {
        return sectorService.save(sector);
    }

    @PostMapping("/sectors/{name}")
    @ResponseBody
    public Sector addPoolToSector(@RequestBody Pool pool, @PathVariable String name) {
        Sector sector = getByName(name);
        if (sector == null) return null;
        sector.addPool(pool);
        return sectorService.save(sector);
    }
}

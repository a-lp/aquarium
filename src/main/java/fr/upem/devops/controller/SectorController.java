package fr.upem.devops.controller;

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
}

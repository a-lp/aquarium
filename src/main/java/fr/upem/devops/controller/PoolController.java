package fr.upem.devops.controller;

import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.Fish;
import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Sector;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PoolController {
    @Autowired
    private PoolService poolService;
    @Autowired
    private SectorService sectorService;

    @GetMapping("/pools")
    public Iterable<Pool> getAll() {
        return poolService.getAll();
    }

    @GetMapping("/pools/{id}")
    public Pool getById(@PathVariable String id) {
        return poolService.getById(Long.parseLong(id));
    }

    @PostMapping("/sectors/{sectorId}/pools")
    @ResponseBody
    public Pool addPool(@RequestBody Pool pool, @PathVariable String sectorId) {
        Sector sector = sectorService.getById(Long.parseLong(sectorId));
        if (sector == null) throw new ResourceNotFoundException("Sector " + sectorId + " not found!");
        pool.setSector(sector);
        return poolService.save(pool);
    }

    @PostMapping("/pools/{id}")
    @ResponseBody
    public Pool addFishToPool(@RequestBody Fish fish, @PathVariable String id) {
        Pool pool = getById(id);
        if (pool == null) return null;
        pool.addFish(fish);
        return poolService.save(pool);
    }

    @PutMapping("/pools/{id}")
    @ResponseBody
    public Pool updatePool(@PathVariable String id, @RequestBody Pool pool) {
        Pool p = getById(id);
        p.setVolume(pool.getVolume());
        p.setMaxCapacity(pool.getMaxCapacity());
        p.setCondition(pool.getCondition());
        p.setResponsible(p.getResponsible());
        p.setFishes(pool.getFishes());
        p.setScheduledActivities(pool.getScheduledActivities());
        p.setSector(p.getSector());
        return poolService.save(p);
    }

    @DeleteMapping("/pools/{id}")
    public Pool deletePool(@PathVariable String id) {
        return poolService.remove(getById(id));
    }
}

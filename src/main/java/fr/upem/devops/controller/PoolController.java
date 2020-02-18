package fr.upem.devops.controller;

import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Sector;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.SectorService;
import fr.upem.devops.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PoolController {
    @Autowired
    private PoolService poolService;
    @Autowired
    private SectorService sectorService;
    @Autowired
    private StaffService staffService;

    @GetMapping("/pools")
    public Iterable<Pool> getAll() {
        return poolService.getAll();
    }

    @GetMapping("/pools/{id}")
    public Pool getById(@PathVariable String id) {
        Pool pool = poolService.getById(Long.parseLong(id));
        if (pool == null) throw new ResourceNotFoundException("Pool with id '" + id + "' not found!");
        return pool;
    }

    @PostMapping("/sectors/{sectorId}/responsible/{staffId}/pools")
    @ResponseBody
    public Pool addPool(@RequestBody Pool pool, @PathVariable String sectorId, @PathVariable String staffId) {
        Sector sector = sectorService.getById(Long.parseLong(sectorId));
        if (sector == null) throw new ResourceNotFoundException("Sector " + sectorId + " not found!");
        Staff staff = staffService.getById(Long.parseLong(staffId));
        if (staff == null) throw new ResourceNotFoundException("staff " + staffId + " not found!");
        pool.setSector(sector);
        pool.setResponsible(staff);
        return poolService.save(pool);
    }

    @PutMapping("/pools/{id}")
    @ResponseBody
    public Pool updatePool(@PathVariable String id, @RequestBody Map<String, String> parameters) {
        Pool p = getById(id);
        if (parameters.containsKey("volume"))
            p.setVolume(Double.parseDouble(parameters.get("volume")));
        if (parameters.containsKey("maxCapacity"))
            p.setMaxCapacity(Long.parseLong(parameters.get("maxCapacity")));
        if (parameters.containsKey("condition"))
            p.setCondition(Pool.WaterCondition.valueOf(parameters.get("condition")));
        //todo: vedere lo staff
//        p.setResponsible(p.getResponsible());
//        p.setFishes(pool.getFishes());
//        p.setSchedules(pool.getSchedules());
//        p.setSector(p.getSector());
        return poolService.save(p);
    }

    @DeleteMapping("/pools/{id}")
    public Pool deletePool(@PathVariable String id) {
        Pool pool = getById(id);
        return poolService.remove(pool);
    }
}

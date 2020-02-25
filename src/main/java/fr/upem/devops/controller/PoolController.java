package fr.upem.devops.controller;

import fr.upem.devops.model.*;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.SectorService;
import fr.upem.devops.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
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
    public Pool getById(@PathVariable String id) throws ResponseStatusException {
        Pool pool = null;
        try {
            pool = poolService.getById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot convert id: '" + id + "' into Long");
        }
        if (pool == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pool with id '" + id + "' not found!");
        return pool;
    }

    @GetMapping("/pools/{id}/fishes")
    public Iterable<Fish> getPoolFishes(@PathVariable String id) {
        Pool pool = getById(id);
        return pool.getFishes();
    }

    @GetMapping("/pools/{id}/schedules")
    public Iterable<Schedule> getPoolSchedules(@PathVariable String id) {
        Pool pool = getById(id);
        return pool.getSchedules();
    }

    @PostMapping("/sectors/{sectorId}/responsible/{staffId}/pools")
    @ResponseBody
    public Pool addPool(@RequestBody Pool pool, @PathVariable String sectorId, @PathVariable String staffId) {
        Sector sector = getSector(sectorId);
        Staff staff = getStaff(staffId);
        pool.setCondition(Pool.WaterCondition.CLEAN);
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
        try {
            if (parameters.containsKey("maxCapacity"))
                p.setMaxCapacity(Long.parseLong(parameters.get("maxCapacity")));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "maxCapacity value '" + parameters.get("maxCapacity") + "' cannot be converted in Long!");
        }
        try {
            if (parameters.containsKey("condition"))
                p.setCondition(Pool.WaterCondition.valueOf(parameters.get("condition")));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "condition value '" + parameters.get("condition") + "' cannot be converted in WaterCondition!");
        }
        if (parameters.containsKey("responsible")) {
            p.setResponsible(getStaff(parameters.get("responsible")));
        }
        if (parameters.containsKey("sector")) {
            p.setSector(getSector(parameters.get("sector")));
        }
        return poolService.save(p);
    }

    @DeleteMapping("/pools/{id}")
    public Pool deletePool(@PathVariable String id) {
        Pool pool = getById(id);
        if (!pool.getFishes().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible to delete the pool! Fishes assigned to pool '" + pool.getId() + "' must be moved to another pool before removing!");
        if (pool.getResponsible() != null)
            pool.getResponsible().removePoolResponsability(pool);
        for (Schedule schedule : pool.getSchedules())
            for (PoolActivity activity : schedule.getScheduledActivities())
                for (Staff staff : activity.getStaffList())
                    staff.setActivities(new HashSet<>());
        return poolService.remove(pool);
    }

    private Sector getSector(String id) {
        Sector sector = null;
        try {
            sector = this.sectorService.getById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot convert sector id: '" + id + "' into Long");
        }
        if (sector == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sector with id '" + id + "' not found!");
        return sector;
    }

    private Staff getStaff(String id) {
        Staff staff = null;
        try {
            staff = this.staffService.getById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot convert staff id: '" + id + "' into Long");
        }
        if (staff == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff with id '" + id + "' not found!");
        return staff;
    }
}

package fr.upem.devops.controller;

import fr.upem.devops.model.*;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.ScheduleService;
import fr.upem.devops.service.SectorService;
import fr.upem.devops.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@RestController
public class ScheduleController {
    @Autowired
    private ScheduleService service;
    @Autowired
    private PoolService poolService;
    @Autowired
    private SectorService sectorService;
    @Autowired
    private StaffService staffService;

    @GetMapping("/api/schedules")
    public Iterable<Schedule> getAll() {
        return service.getAll();
    }

    @GetMapping("/api/schedules/{id}")
    public Schedule getById(@PathVariable String id) {
        Schedule schedule = null;
        try {
            schedule = service.getById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot convert id: '" + id + "' into Long");
        }
        if (schedule == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule with id '" + id + "' not found!");
        return schedule;
    }

    @GetMapping("/api/schedules/{id}/activities")
    public Set<PoolActivity> getActivities(@PathVariable String id) {
        Schedule schedule = getById(id);
        return schedule.getScheduledActivities();
    }

    @GetMapping("/api/schedules/staff/{id}")
    public Set<Schedule> getSchedulesByResponsible(@PathVariable String id) {
        Staff staff = staffService.getById(Long.parseLong(id));
        Set<Schedule> schedules = new HashSet<>();
        for (Pool pool : staff.getPoolsResponsabilities()) {
            schedules.addAll(pool.getSchedules());
        }
        return schedules;
    }

    @GetMapping("/api/schedules/{id}/staff")
    public Set<Staff> getStaffBySectorFromPool(@PathVariable String id) {
        Schedule schedule = getById(id);
        Pool pool = poolService.getById(schedule.getPool().getId());
        Sector sector = sectorService.getByName(pool.getSector().getName());
        return sector.getStaffList();
    }

    @PostMapping("/api/pools/{poolId}/schedules")
    @ResponseBody
    public Schedule addSchedule(@RequestBody Schedule schedule, @PathVariable String poolId) {
        Pool pool = getPool(poolId);
        schedule.setPool(pool);
        return service.save(schedule);
    }

    @PutMapping("/api/schedules/{id}")
    @ResponseBody
    public Schedule updateSchedule(@PathVariable String id, @RequestBody HashMap<String, String> schedule) {
        Schedule p = getById(id);
        try {
            if (schedule.containsKey("startPeriod"))
                p.setStartPeriod(new Date(Long.parseLong(schedule.get("startPeriod"))));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error converting startPeriod: '" + schedule.get("startPeriod") + "' into Date!");
        }
        try {
            if (schedule.containsKey("endPeriod"))
                p.setEndPeriod(new Date(Long.parseLong(schedule.get("endPeriod"))));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error converting startPeriod'" + schedule.get("endPeriod") + "' into Date!");
        }
        if (schedule.containsKey("pool")) {
            p.setPool(getPool(schedule.get("pool")));
        }
        return service.save(p);
    }

    @DeleteMapping("/api/schedules/{id}")
    public Schedule deleteSchedule(@PathVariable String id) {
        Schedule schedule = getById(id);
        schedule.getPool().removeSchedule(schedule);
        return service.remove(schedule);
    }

    private Pool getPool(String id) {
        Pool pool = null;
        try {
            pool = this.poolService.getById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot convert pool id: '" + id + "' into Long");
        }
        if (pool == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pool with id '" + id + "' not found!");
        return pool;
    }
}

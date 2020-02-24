package fr.upem.devops.controller;

import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.Pool;
import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.model.Schedule;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

@RestController
public class ScheduleController {
    @Autowired
    private ScheduleService service;
    @Autowired
    private PoolService poolService;

    @GetMapping("/schedules")
    public Iterable<Schedule> getAll() {
        return service.getAll();
    }

    @GetMapping("/schedules/{id}")
    public Schedule getById(@PathVariable String id) {
        Schedule schedule = service.getById(Long.parseLong(id));
        if (schedule == null) throw new ResourceNotFoundException("Schedule with id '" + id + "' not found!");
        return schedule;
    }

    @GetMapping("/schedules/{id}/activities")
    public Set<PoolActivity> getActivities(@PathVariable String id) {
        Schedule schedule = getById(id);
        return schedule.getScheduledActivities();
    }

    @PostMapping("pools/{poolId}/schedules")
    @ResponseBody
    public Schedule addSchedule(@RequestBody Schedule schedule, @PathVariable String poolId) {
        Pool pool = poolService.getById(Long.parseLong(poolId));
        if (pool == null) throw new ResourceNotFoundException("Pool with id '" + poolId + "' not found!");
        schedule.setPool(pool);
        return service.save(schedule);
    }

    @PutMapping("/schedules/{id}")
    @ResponseBody
    public Schedule updateSchedule(@PathVariable String id, @RequestBody HashMap<String, String> schedule) {
        Schedule p = getById(id);
//        p.setScheduledActivities(schedule.getScheduledActivities());
        if (schedule.containsKey("startPeriod"))
            p.setStartPeriod(new Date(Long.parseLong(schedule.get("startPeriod"))));
        if (schedule.containsKey("endPeriod"))
            p.setEndPeriod(new Date(Long.parseLong(schedule.get("endPeriod"))));
        if (schedule.containsKey("pool")) {
            p.getPool().removeSchedule(p);
            p.setPool(poolService.getById(Long.parseLong(schedule.get("pool"))));
        }
        return service.save(p);
    }

    @DeleteMapping("/schedules/{id}")
    public Schedule deleteSchedule(@PathVariable String id) {
        return service.remove(getById(id));
    }

}

package fr.upem.devops.controller;

import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Schedule;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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
    public Schedule updateSchedule(@PathVariable String id, @RequestBody HashMap<String, Object> schedule) {
        Schedule p = getById(id);
//        p.setScheduledActivities(schedule.getScheduledActivities());
        //TODO: fix date
//        p.setEndPeriod(schedule.get("endPeriod"));
//        p.setStartPeriod(schedule.get("startPeriod"));
//        p.setPool(schedule.getPool());
        return service.save(p);
    }

    @DeleteMapping("/schedules/{id}")
    public Schedule deleteSchedule(@PathVariable String id) {
        return service.remove(getById(id));
    }

}

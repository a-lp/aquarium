package fr.upem.devops.controller;

import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Schedule;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return service.getById(Long.parseLong(id));
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
    public Schedule updateSchedule(@PathVariable String id, @RequestBody Schedule schedule) {
        Schedule p = getById(id);
        p.setScheduledActivities(schedule.getScheduledActivities());
        p.setEndPeriod(schedule.getEndPeriod());
        p.setPool(schedule.getPool());
        p.setStartPeriod(schedule.getStartPeriod());
        return service.save(p);
    }

    @DeleteMapping("/schedules/{id}")
    public Schedule deleteSchedule(@PathVariable String id) {
        return service.remove(getById(id));
    }

}

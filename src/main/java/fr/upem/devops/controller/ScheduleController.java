package fr.upem.devops.controller;

import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.model.Schedule;
import fr.upem.devops.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScheduleController {
    @Autowired
    private ScheduleService service;

    @GetMapping("/schedules")
    public Iterable<Schedule> getAll() {
        return service.getAll();
    }

    @GetMapping("/schedules/{id}")
    public Schedule getById(@PathVariable String id) {
        return service.getById(Long.parseLong(id));
    }

    @PostMapping("/schedules")
    @ResponseBody
    public Schedule addSchedule(@RequestBody Schedule schedule) {
        return service.save(schedule);
    }

    @PostMapping("/schedules/{id}/assign-activity")
    @ResponseBody
    public Schedule assignPoolActivityToSchedule(@RequestBody PoolActivity poolActivity, @PathVariable String id) {
        Schedule schedule = getById(id);
        if (schedule == null) return null;
        schedule.assignActivity(poolActivity);
        return service.save(schedule);
    }

}

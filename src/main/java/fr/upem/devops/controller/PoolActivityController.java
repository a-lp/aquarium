package fr.upem.devops.controller;

import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.PoolActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PoolActivityController {
    @Autowired
    private PoolActivityService service;

    @GetMapping("/activities")
    public Iterable<PoolActivity> getAll() {
        return service.getAll();
    }

    @GetMapping("/activities/{id}")
    public PoolActivity getById(@PathVariable String id) {
        return service.getById(Long.parseLong(id));
    }

    @PostMapping("/activities")
    @ResponseBody
    public PoolActivity addPoolActivity(@RequestBody PoolActivity activities) {
        return service.save(activities);
    }

    @PostMapping("/activities/{id}/assign-staff")
    @ResponseBody
    public PoolActivity assignStaffToPoolActivity(@RequestBody Staff staff, @PathVariable String id) {
        PoolActivity activities = getById(id);
        if (activities == null) return null;
        activities.assignStaff(staff);
        return service.save(activities);
    }
}

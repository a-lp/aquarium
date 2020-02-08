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
        //TODO: manage null
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

    @PutMapping("/activities/{id}")
    @ResponseBody
    public PoolActivity updatePoolActivity(@PathVariable String id, @RequestBody PoolActivity poolActivityRequest) {
        System.out.println(poolActivityRequest);
        PoolActivity poolActivity = getById(poolActivityRequest.getId().toString());
        poolActivity.setSchedule(poolActivityRequest.getSchedule());
        poolActivity.setDescription(poolActivityRequest.getDescription());
        poolActivity.setOpenToPublic(poolActivityRequest.getOpenToPublic());
        poolActivity.setEndActivity(poolActivityRequest.getEndActivity());
        poolActivity.setStartActivity(poolActivityRequest.getStartActivity());
        poolActivity.setStaffList(poolActivityRequest.getStaffList());
        return service.save(poolActivity);
    }

    @DeleteMapping("/activities/{id}")
    public PoolActivity deleteActivity(@PathVariable String id) {
        return service.remove(getById(id));
    }
}

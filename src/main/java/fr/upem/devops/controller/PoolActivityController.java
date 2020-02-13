package fr.upem.devops.controller;

import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.model.Schedule;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.PoolActivityService;
import fr.upem.devops.service.ScheduleService;
import fr.upem.devops.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PoolActivityController {
    @Autowired
    private PoolActivityService service;

    @Autowired
    private StaffService staffService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/activities")
    public Iterable<PoolActivity> getAll() {
        return service.getAll();
    }

    @GetMapping("/activities/{id}")
    public PoolActivity getById(@PathVariable String id) {
        //TODO: manage null
        return service.getById(Long.parseLong(id));
    }

    @PostMapping("/schedule/{scheduleId}/activities/staff/{staffResponsible}")
    @ResponseBody
    public PoolActivity addPoolActivity(@RequestBody PoolActivity activity, @PathVariable String scheduleId, @PathVariable List<String> staffResponsible) {
        Schedule schedule = scheduleService.getById(Long.parseLong(scheduleId));
        if (schedule == null) throw new ResourceNotFoundException("Schedule with id '" + scheduleId + "' not found!");
        List<Staff> staffs = new ArrayList<>();
        for (String id : staffResponsible) {
            Staff s = staffService.getById(Long.parseLong(id));
            if (s == null)
                throw new ResourceNotFoundException("Staff with id '" + id + "' not found!");
            staffs.add(s);
        }
        activity.setStaffList(staffs);
        activity.setSchedule(schedule);
        return service.save(activity);
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
        poolActivity.setRepeated(poolActivityRequest.getRepeated());
        return service.save(poolActivity);
    }

    @DeleteMapping("/activities/{id}")
    public PoolActivity deleteActivity(@PathVariable String id) {
        return service.remove(getById(id));
    }
}

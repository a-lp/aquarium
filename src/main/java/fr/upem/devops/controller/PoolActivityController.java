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

import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        PoolActivity activity = service.getById(Long.parseLong(id));
        if (activity == null) throw new ResourceNotFoundException("Activity with id '" + id + "' not found!");
        return activity;
    }

    @PostMapping("/schedule/{scheduleId}/activities/staff/{staffResponsible}")
    @ResponseBody
    public PoolActivity addPoolActivity(@RequestBody PoolActivity activity, @PathVariable String scheduleId, @PathVariable List<String> staffResponsible) {
        Schedule schedule = scheduleService.getById(Long.parseLong(scheduleId));
        if (schedule == null) throw new ResourceNotFoundException("Schedule with id '" + scheduleId + "' not found!");
        Set<Staff> staffs = new HashSet<>();
        for (String id : staffResponsible) {
            Staff s = staffService.getById(Long.parseLong(id));
            if (s == null)
                throw new ResourceNotFoundException("Staff with id '" + id + "' not found!");
            s.assignActivity(activity);
            staffs.add(s);
        }
        activity.setStaffList(staffs);
        activity.setSchedule(schedule);
        return service.save(activity);
    }


    @PutMapping("/activities/{id}")
    @ResponseBody
    public PoolActivity updatePoolActivity(@PathVariable String id, @RequestBody HashMap<String, String> parameters) {
        PoolActivity poolActivity = getById(id);
        if (parameters.containsKey("description"))
            poolActivity.setDescription(parameters.get("description"));
        if (parameters.containsKey("startActivity"))
            poolActivity.setStartActivity(LocalTime.parse(parameters.get("startActivity")));
        if (parameters.containsKey("endActivity"))
            poolActivity.setEndActivity(LocalTime.parse(parameters.get("endActivity")));
        if (parameters.containsKey("openToPublic"))
            poolActivity.setOpenToPublic(Boolean.valueOf(parameters.get("openToPublic")));
        if (parameters.containsKey("repeated"))
            poolActivity.setRepeated(Boolean.valueOf(parameters.get("repeated")));
        if (parameters.containsKey("schedule")) {
            Schedule schedule = scheduleService.getById(Long.parseLong(parameters.get("schedule")));
            poolActivity.setSchedule(schedule);
        }
        if (parameters.containsKey("staffList")) {
            poolActivity.getStaffList().forEach(staff -> staff.removeActivity(poolActivity));
            if (!parameters.get("staffList").isEmpty()) {
                String[] staffIds = parameters.get("staffList").split(",");
                Set<Staff> staffList = new HashSet<>();
                for (String staff_id : staffIds) {
                    Staff staff = staffService.getById(Long.parseLong(staff_id));
                    staff.assignActivity(poolActivity);
                    staffList.add(staff);
                }
                poolActivity.setStaffList(staffList);
            }
        }
        return service.save(poolActivity);
    }

    @DeleteMapping("/activities/{id}")
    public PoolActivity deleteActivity(@PathVariable String id) {
        return service.remove(getById(id));
    }
}

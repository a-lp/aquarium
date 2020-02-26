package fr.upem.devops.controller;

import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.model.Schedule;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.PoolActivityService;
import fr.upem.devops.service.ScheduleService;
import fr.upem.devops.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class PoolActivityController {
    @Autowired
    private PoolActivityService service;

    @Autowired
    private StaffService staffService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/api/activities")
    public Iterable<PoolActivity> getAll() {
        return service.getAll();
    }

    @GetMapping("/api/activities/{id}")
    public PoolActivity getById(@PathVariable String id) {
        PoolActivity activity = null;
        try {
            activity = service.getById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot convert id: '" + id + "' into Long");
        }
        if (activity == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity with id: '" + id + "' not found!");
        return activity;
    }

    @PostMapping("/api/schedule/{scheduleId}/activities/staff/{staffResponsible}")
    @ResponseBody
    public PoolActivity addPoolActivity(@RequestBody PoolActivity activity, @PathVariable String scheduleId, @PathVariable List<String> staffResponsible) {
        Schedule schedule = getSchedule(scheduleId);
        Set<Staff> staffs = assignStaff(activity, staffResponsible);
        activity.setStaffList(staffs);
        activity.setSchedule(schedule);
        return service.save(activity);
    }


    @PutMapping("/api/activities/{id}")
    @ResponseBody
    public PoolActivity updatePoolActivity(@PathVariable String id, @RequestBody HashMap<String, String> parameters) {
        PoolActivity poolActivity = getById(id);
        if (parameters.containsKey("description"))
            poolActivity.setDescription(parameters.get("description"));
        try {
            if (parameters.containsKey("startActivity"))
                poolActivity.setStartActivity(LocalTime.parse(parameters.get("startActivity")));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error converting startActivity: '" + parameters.get("startActivity") + "' into LocalTime!");
        }
        try {
            if (parameters.containsKey("endActivity"))
                poolActivity.setEndActivity(LocalTime.parse(parameters.get("endActivity")));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error converting endActivity: '" + parameters.get("endActivity") + "' into LocalTime!");
        }
        try {
            if (parameters.containsKey("openToPublic"))
                poolActivity.setOpenToPublic(Boolean.valueOf(parameters.get("openToPublic")));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error converting openToPublic: '" + parameters.get("openToPublic") + "' into Boolean!");
        }
        try {
            if (parameters.containsKey("repeated"))
                poolActivity.setRepeated(Boolean.valueOf(parameters.get("repeated")));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error converting repeated: '" + parameters.get("repeated") + "' into Boolean!");
        }
        if (parameters.containsKey("schedule")) {
            Schedule schedule = getSchedule(parameters.get("schedule"));
            poolActivity.setSchedule(schedule);
        }
        if (parameters.containsKey("staffList")) {
            if (!parameters.get("staffList").isEmpty()) {
                List<Staff> staffs = Arrays.stream(parameters.get("staffList").split(",")).map(x -> getStaff(x)).collect(Collectors.toList());
                if (staffs.size() == 0)
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activities must have at least one responsible in charge!");
                poolActivity.getStaffList().forEach(staff -> staff.removeActivity(poolActivity));
                Set<Staff> staffList = new HashSet<>();
                for (Staff staff : staffs) {
                    staff.assignActivity(poolActivity);
                    staffList.add(staff);
                }
                poolActivity.setStaffList(staffList);
            } else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activities must have at least one responsible in charge!");
        }
        return service.save(poolActivity);
    }


    @DeleteMapping("/api/activities/{id}")
    public PoolActivity deleteActivity(@PathVariable String id) {
        PoolActivity poolActivity = getById(id);
        for (Staff staff : poolActivity.getStaffList())
            staff.removeActivity(poolActivity);
        return service.remove(getById(id));
    }


    private Set<Staff> assignStaff(PoolActivity activity, List<String> staffResponsible) {
        Set<Staff> staffs = new HashSet<>();
        for (String id : staffResponsible) {
            staffs.add(getStaff(id));
        }
        for (Staff s : staffs) s.assignActivity(activity);
        return staffs;
    }

    private Schedule getSchedule(String id) {
        Schedule schedule = null;
        try {
            schedule = this.scheduleService.getById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot convert schedule id: '" + id + "' into Long");
        }
        if (schedule == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule with id '" + id + "' not found!");
        return schedule;
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

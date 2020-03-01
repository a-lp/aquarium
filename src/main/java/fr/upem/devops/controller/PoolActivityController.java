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
        if (activity.getDay() == null && !activity.getRepeated())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activities must have a day or need to be repeated!");
        if (activity.getDay() != null)
            activity.setRepeated(false);
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
            if (parameters.containsKey("startActivity") && parameters.containsKey("endActivity")) {
                LocalTime start = LocalTime.parse(parameters.get("startActivity"));
                LocalTime end = LocalTime.parse(parameters.get("endActivity"));
                if (start.isBefore(end)) {
                    poolActivity.setStartActivity(start);
                    poolActivity.setEndActivity(end);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start activity time cannot be after end activity time!");
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error converting '" + parameters.get("startActivity") + "' into LocalTime!");
        }
        if (parameters.containsKey("schedule")) {
            Schedule schedule = getSchedule(parameters.get("schedule"));
            poolActivity.setSchedule(schedule);
        }
        try {
            if (parameters.containsKey("repeated")) {
                poolActivity.setRepeated(Boolean.valueOf(parameters.get("repeated")));
                if (poolActivity.getRepeated())
                    poolActivity.setDay(null);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error converting repeated: '" + parameters.get("repeated") + "' into Boolean!");
        }
        try {
            if (parameters.containsKey("day")) {
                Date day = new Date(Long.parseLong(parameters.get("day")));
                if (day.compareTo(poolActivity.getSchedule().getStartPeriod()) >= 0 && day.compareTo(poolActivity.getSchedule().getEndPeriod()) <= 0) {
                    poolActivity.setDay(day);
                    poolActivity.setRepeated(false);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Day must be included between schedule start period (" + poolActivity.getSchedule().getStartPeriod().toString() + ") and end period (" + poolActivity.getSchedule().getEndPeriod().toString() + ")");
                }
            }
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error converting day: '" + parameters.get("day") + "' into Date!");
        }
        try {
            if (parameters.containsKey("openToPublic"))
                poolActivity.setOpenToPublic(Boolean.valueOf(parameters.get("openToPublic")));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error converting openToPublic: '" + parameters.get("openToPublic") + "' into Boolean!");
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

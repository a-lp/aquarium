package fr.upem.devops.controller;

import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.model.Sector;
import fr.upem.devops.model.Staff;
import fr.upem.devops.model.User;
import fr.upem.devops.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class StaffController {
    @Autowired
    private StaffService service;

    @GetMapping("/api/staff")
    public Iterable<Staff> getAll() {
        return service.getAll();
    }

    @GetMapping("/api/staff/{id}")
    public Staff getById(@PathVariable String id) {
        Staff staff = null;
        try {
            staff = service.getById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot convert id: '" + id + "' into Long");
        }
        if (staff == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff with id: '" + id + "' not found!");
        return staff;
    }

    @GetMapping("/api/staff/profiles")
    public Iterable<User> profiles() {
        return service.getProfiles();
    }

    @GetMapping("/api/staff/{id}/sectors")
    public List<Sector> getSectors(@PathVariable String id) {
        Staff staff = getById(id);
        return staff.getSectors();
    }

    @GetMapping("/api/staff/role/{role}")
    public Iterable<Staff> getByRole(@PathVariable String role) {
        return service.getByRole(Staff.StaffRole.valueOf(role));
    }

    @GetMapping("/api/staff/{id}/activities")
    public Set<PoolActivity> getActivities(@PathVariable String id) {
        Staff staff = getById(id);
        return staff.getActivities();
    }

    @PostMapping("/api/staff")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Staff addStaff(@RequestBody Staff staff) {
        return service.save(staff);
    }

    @PutMapping("/api/staff/{id}")
    @ResponseBody
    public Staff updateStaff(@PathVariable String id, @RequestBody Map<String, String> parameters) {
        Staff p = getById(id);

        if (parameters.containsKey("name"))
            p.setName(parameters.get("name"));
        if (parameters.containsKey("address"))
            p.setAddress(parameters.get("address"));
        if (parameters.containsKey("surname"))
            p.setSurname(parameters.get("surname"));
        if (parameters.containsKey("address"))
            p.setAddress(parameters.get("address"));
        if (parameters.containsKey("socialSecurity"))
            p.setSocialSecurity(parameters.get("socialSecurity"));
        try {
            if (parameters.containsKey("birthday"))
                p.setBirthday(new Date(Long.parseLong(parameters.get("birthday"))));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'" + parameters.get("birthday") + "' is not a valid value to be converted in date. Insert a Long representing the time in milliseconds");
        }
        try {
            if (parameters.containsKey("role"))
                p.setRole(Staff.StaffRole.valueOf(parameters.get("role")));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'" + parameters.get("role") + "' is not a valid value to be converted in StaffRole.");
        }

        return service.save(p);
    }

    @DeleteMapping("/api/staff/{id}")
    public Staff deleteStaff(@PathVariable String id) {
        Staff staff = getById(id);
        for (PoolActivity activity : staff.getActivities()) {
            if (activity.getStaffList().size() == 1)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activities must have at least one responsible! Removing this staff, will also remove the only one responsible");
            activity.removeStaff(staff);
        }
        for (Sector sector : staff.getSectors()) {
            if (sector.getStaffList().size() == 1)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sectors must have at least one responsible! Removing this staff, will also remove the only one responsible");
            sector.removeStaff(staff);
        }
        return service.remove(staff);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error during conversione of the input request!")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    void onHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
    }
}

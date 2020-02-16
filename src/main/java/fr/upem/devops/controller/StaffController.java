package fr.upem.devops.controller;

import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StaffController {
    @Autowired
    private StaffService service;

    @GetMapping("/staff")
    public Iterable<Staff> getAll() {
        Iterable<Staff> result = service.getAll();
        return result;
    }

    @GetMapping("/staff/{id}")
    public Staff getById(@PathVariable String id) {
        Staff staff = service.getById(Long.parseLong(id));
        if (staff == null) throw new ResourceNotFoundException("Staff with id: '" + id + "' not found!");
        return staff;
    }

    @PostMapping("/staff")
    @ResponseBody
    public Staff addStaff(@RequestBody Staff staff) {
        return service.save(staff);
    }

    @PutMapping("/staff/{id}")
    @ResponseBody
    public Staff updateStaff(@PathVariable String id, @RequestBody Staff staff) {
        Staff p = getById(id);
        p.setName(staff.getName());
        p.setAddress(staff.getAddress());
        p.setBirthday(staff.getBirthday());
        p.setPoolsResponsabilities(staff.getPoolsResponsabilities());
        p.setRole(staff.getRole());
        p.setSectors(staff.getSectors());
        p.setSocialSecurity(staff.getSocialSecurity());
        p.setSurname(staff.getSurname());
        return service.save(p);
    }

    @DeleteMapping("/staff/{id}")
    public Staff deleteStaff(@PathVariable String id) {
        return service.remove(getById(id));
    }
}

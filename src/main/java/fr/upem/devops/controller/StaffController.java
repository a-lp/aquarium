package fr.upem.devops.controller;

import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

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
    public Staff updateStaff(@PathVariable String id, @RequestBody Map<String, String> parameters) {
        Staff p = getById(id);
        if (parameters.containsKey("name"))
            p.setName(parameters.get("name"));
        if (parameters.containsKey("address"))
            p.setAddress(parameters.get("address"));
        if (parameters.containsKey("address"))
            p.setAddress(parameters.get("address"));
        if (parameters.containsKey("birthday"))
            p.setBirthday(new Date(Long.parseLong(parameters.get("birthday"))));
//        if (parameters.containsKey("poolsResponsabilities"))
//            p.setPoolsResponsabilities(Long.parseLong(parameters.get("birthday")));
//        p.setSectors(staff.getSectors());

        if (parameters.containsKey("role"))
            p.setRole(Staff.StaffRole.valueOf(parameters.get("role")));
        if (parameters.containsKey("socialSecurity"))
            p.setSocialSecurity(parameters.get("socialSecurity"));
        if (parameters.containsKey("surname"))
            p.setSurname(parameters.get("surname"));
        return service.save(p);
    }

    @DeleteMapping("/staff/{id}")
    public Staff deleteStaff(@PathVariable String id) {
        return service.remove(getById(id));
    }
}

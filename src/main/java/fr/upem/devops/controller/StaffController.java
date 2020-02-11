package fr.upem.devops.controller;

import fr.upem.devops.model.Pool;
import fr.upem.devops.model.PoolActivity;
import fr.upem.devops.model.Sector;
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
        return service.getAll();
    }

    @GetMapping("/staff/{id}")
    public Staff getById(@PathVariable String id) {
        return service.getById(Long.parseLong(id));
    }

    @PostMapping("/staff")
    @ResponseBody
    public Staff addStaff(@RequestBody Staff staff) {
        return service.save(staff);
    }

    @PostMapping("/staff/{id}/assign-pool")
    @ResponseBody
    public Staff assignPoolToStaff(@RequestBody Pool pool, @PathVariable String id) {
        Staff staff = getById(id);
        if (staff == null) return null;
        staff.assignPool(pool);
        return service.save(staff);
    }

    @PostMapping("/staff/{id}/assign-sector")
    @ResponseBody
    public Staff assignSectorToStaff(@RequestBody Sector sector, @PathVariable String id) {
        Staff staff = getById(id);
        if (staff == null) return null;
        staff.assignSector(sector);
        return service.save(staff);
    }

    @PostMapping("/staff/{id}/assign-activity")
    @ResponseBody
    public Staff assignActivityToStaff(@RequestBody PoolActivity activity, @PathVariable String id) {
        Staff staff = getById(id);
        if (staff == null) return null;
        staff.assignActivity(activity);
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

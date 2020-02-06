package fr.upem.devops.controller;

import fr.upem.devops.model.Pool;
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
}

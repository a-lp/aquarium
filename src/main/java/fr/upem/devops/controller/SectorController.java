package fr.upem.devops.controller;

import fr.upem.devops.model.Fish;
import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Sector;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.SectorService;
import fr.upem.devops.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class SectorController {
    @Autowired
    private SectorService sectorService;

    @Autowired
    private StaffService staffService;

    @GetMapping("/api/sectors")
    public Iterable<Sector> getAll() {
        return sectorService.getAll();
    }

    @GetMapping("/api/sectors/{name}")
    public Sector getByName(@PathVariable String name) {
        Sector sector = sectorService.getByName(name);
        if (sector == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sector named '" + name + "' not found!");
        return sector;
    }

    @GetMapping("/api/sectors/id/{id}")
    public Sector getById(@PathVariable String id) {
        Sector sector = null;
        try {
            sector = sectorService.getById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot convert id: '" + id + "' into Long");
        }
        if (sector == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sector with id: '" + id + "' not found!");
        return sector;
    }

    @GetMapping("/api/sectors/{name}/staffs")
    public Iterable<Staff> getSectorStaffs(@PathVariable String name) {
        Sector sector = getByName(name);
        return sector.getStaffList();
    }

    @GetMapping("/api/sectors/{name}/pools")
    public Iterable<Pool> getSectorPools(@PathVariable String name) {
        Sector sector = getByName(name);
        return sector.getPools();
    }

    @PostMapping("/api/sectors/responsible/{ids}")
    @ResponseBody
    public Sector addSector(@RequestBody Sector sector, @PathVariable List<String> ids) {
        try {
            getByName(sector.getName());
        } catch (ResponseStatusException e) {
            Set<Staff> staffs = new HashSet<>();
            for (String id : ids) {
                Staff s = getStaff(id);
                staffs.add(s);
            }
            sector.setStaffList(staffs);
            return sectorService.save(sector);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate name for sector '" + sector.getName());
    }

    @PutMapping("/api/sectors/{id}")
    @ResponseBody
    public Sector updateSector(@PathVariable String id, @RequestBody HashMap<String, String> parameters) {
        Sector p = getById(id);
        if (parameters.containsKey("name"))
            p.setName(parameters.get("name"));
        if (parameters.containsKey("location"))
            p.setLocation(parameters.get("location"));
        if (parameters.containsKey("staffList")) {
            if (parameters.get("staffList").isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot update sector, no staff found! There must be at least one responsible");
            List<Long> ids = null;
            try {
                ids = Arrays.stream(parameters.get("staffList").split(",")).map(Long::parseLong).collect(Collectors.toList());
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during conversion of staff ids in Long!");
            }
            for (Staff s : p.getStaffList()) s.removeSector(p);
            Set<Staff> staffs = new HashSet<>();
            for (Long staff_id : ids) {
                Staff s = getStaff(staff_id.toString());
                staffs.add(s);
            }
            p.setStaffList(staffs);
        }
        return sectorService.save(p);
    }

    @DeleteMapping("/api/sectors/{name}")
    public Sector deleteSector(@PathVariable String name) {
        Sector sector = getByName(name);
        for (Staff staff : sector.getStaffList())
            staff.removeStaff(staff);
        if(!sector.getPools().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete sector! There are pools in this sector.");

        return sectorService.remove(sector);
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

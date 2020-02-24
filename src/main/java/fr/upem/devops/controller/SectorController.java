package fr.upem.devops.controller;

import fr.upem.devops.errors.ConflictException;
import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Sector;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.SectorService;
import fr.upem.devops.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class SectorController {
    @Autowired
    private SectorService sectorService;

    @Autowired
    private StaffService staffService;

    @GetMapping("/sectors")
    public Iterable<Sector> getAll() {
        return sectorService.getAll();
    }

    @GetMapping("/sectors/{name}")
    public Sector getByName(@PathVariable String name) {
        Sector sector = sectorService.getByName(name);
        if (sector == null) throw new ResourceNotFoundException("Sector  '" + name + "' not found!");
        return sector;
    }

    @GetMapping("/sectors/{id}")
    public Sector getById(@PathVariable String id) {
        Sector sector = sectorService.getById(Long.parseLong(id));
        if (sector == null) throw new ResourceNotFoundException("Sector with id '" + id + "' not found!");
        return sector;
    }

    @GetMapping("/sectors/{name}/staffs")
    public Iterable<Staff> getSectorStaffs(@PathVariable String name) {
        Sector sector = sectorService.getByName(name);
        if (sector == null) throw new ResourceNotFoundException("Sector with id '" + name + "' not found!");
        return sector.getStaffList();
    }

    @GetMapping("/sectors/{name}/pools")
    public Iterable<Pool> getSectorPools(@PathVariable String name) {
        Sector sector = sectorService.getByName(name);
        if (sector == null) throw new ResourceNotFoundException("Sector with id '" + name + "' not found!");
        return sector.getPools();
    }

    @PostMapping("/sectors/responsible/{ids}")
    @ResponseBody
    public Sector addSector(@RequestBody Sector sector, @PathVariable List<String> ids) {
        Set<Staff> staffs = new HashSet<>();
        for (String id : ids) {
            Staff s = staffService.getById(Long.parseLong(id));
            if (s == null)
                throw new ResourceNotFoundException("Staff with id '" + id + "' not found!");
            staffs.add(s);
        }
        if (sectorService.getByName(sector.getName()) != null)
            throw new ConflictException("Another sector named '" + sector.getName() + "' found!");
        sector.setStaffList(staffs);
        return sectorService.save(sector);
    }

    @PutMapping("/sectors/{id}")
    @ResponseBody
    public Sector updateSector(@PathVariable String id, @RequestBody HashMap<String, String> parameters) {
        Sector p = getById(id);
        if (parameters.containsKey("name"))
            p.setName(parameters.get("name"));
        if (parameters.containsKey("location"))
            p.setLocation(parameters.get("location"));
        if (parameters.containsKey("staffList")) {
            for (Staff s : p.getStaffList()) s.removeSector(p);
            String[] ids = parameters.get("staffList").split(",");
            Set<Staff> staffs = new HashSet<>();
            for (String staff_id : ids) {
                Staff s = staffService.getById(Long.parseLong(staff_id));
                if (s == null)
                    throw new ResourceNotFoundException("Staff with id '" + id + "' not found!");
                staffs.add(s);
            }
            p.setStaffList(staffs);
        }
//        p.setPools(parameters.getPools());
        return sectorService.save(p);
    }

    @DeleteMapping("/sectors/{name}")
    public Sector deleteSector(@PathVariable String name) {
        return sectorService.remove(getByName(name));
    }
}

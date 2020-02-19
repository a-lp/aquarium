package fr.upem.devops.controller;

import fr.upem.devops.errors.ConflictException;
import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.Sector;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.SectorService;
import fr.upem.devops.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @PutMapping("/sectors/{name}")
    @ResponseBody
    public Sector updateSector(@PathVariable String name, @RequestBody HashMap<String, String> parameters) {
        Sector p = getByName(name);
        if(parameters.containsKey("name"))
            p.setName(parameters.get("name"));
        if(parameters.containsKey("location"))
            p.setLocation(parameters.get("location"));
//        p.setPools(parameters.getPools());
        return sectorService.save(p);
    }

    @DeleteMapping("/sectors/{name}")
    public Sector deleteSector(@PathVariable String name) {
        return sectorService.remove(getByName(name));
    }
}

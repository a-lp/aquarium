package fr.upem.devops.controller;

import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Sector;
import fr.upem.devops.model.Staff;
import fr.upem.devops.service.SectorService;
import fr.upem.devops.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        if(sector==null) throw new ResourceNotFoundException("Sector named '" + name + "' not found!");
        return sector;
    }

    @PostMapping("/sectors/responsible/{ids}")
    @ResponseBody
    public Sector addSector(@RequestBody Sector sector, @PathVariable List<String> ids) {
        List<Staff> staffs = new ArrayList<>();
        for (String id : ids) {
            Staff s = staffService.getById(Long.parseLong(id));
            if (s == null)
                throw new ResourceNotFoundException("Staff " + id + "not found!");
            staffs.add(s);
        }
        if (getByName(sector.getName()) != null) return null;
        sector.setStaffList(staffs);
        return sectorService.save(sector);
    }

    @PutMapping("/sectors/{name}")
    @ResponseBody
    public Sector updateSector(@PathVariable String name, @RequestBody Sector sector) {
        Sector p = getByName(name);
        p.setLocation(sector.getLocation());
        p.setPools(sector.getPools());
        return sectorService.save(p);
    }

    @DeleteMapping("/sectors/{name}")
    public Sector deleteSector(@PathVariable String name) {
        return sectorService.remove(getByName(name));
    }
}

package fr.upem.devops.controller;

import fr.upem.devops.model.Specie;
import fr.upem.devops.service.SpecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpecieController {
    @Autowired
    private SpecieService specieService;

    @GetMapping("/species")
    public Iterable<Specie> getAll() {
        return specieService.getAll();
    }

    @GetMapping("/species/{name}")
    public Specie getByName(@PathVariable String name) {
        return specieService.getByName(name);
    }

    @PostMapping("/species")
    @ResponseBody
    public Specie addSpecie(@RequestBody Specie specie) {
        if(getByName(specie.getName())!=null) return null;
        //TODO: manage duplicate name
        return specieService.save(specie);
    }

    @PutMapping("/species/{name}")
    @ResponseBody
    public Specie updateSpecie(@PathVariable String name, @RequestBody Specie specie) {
        Specie p = getByName(name);
        p.setAlimentation(specie.getAlimentation());
        p.setExtinctionLevel(specie.getExtinctionLevel());
        p.setFishList(specie.getFishList());
        p.setLifeSpan(specie.getLifeSpan());
        return specieService.save(p);
    }

    @DeleteMapping("/species/{name}")
    public Specie deleteSpecie(@PathVariable String name) {
        return specieService.remove(getByName(name));
    }
}

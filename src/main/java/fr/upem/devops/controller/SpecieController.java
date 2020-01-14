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

        return specieService.save(specie);
    }
}

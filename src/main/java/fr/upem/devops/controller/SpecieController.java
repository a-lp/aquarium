package fr.upem.devops.controller;

import fr.upem.devops.model.Animal;
import fr.upem.devops.model.Specie;
import fr.upem.devops.service.SpecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}

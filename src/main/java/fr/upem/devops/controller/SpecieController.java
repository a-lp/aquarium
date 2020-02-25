package fr.upem.devops.controller;

import fr.upem.devops.model.Alimentation;
import fr.upem.devops.model.Specie;
import fr.upem.devops.service.FishService;
import fr.upem.devops.service.SpecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class SpecieController {
    @Autowired
    private SpecieService specieService;
    @Autowired
    private FishService fishService;

    @GetMapping("/species")
    public Iterable<Specie> getAll() {
        return specieService.getAll();
    }

    @GetMapping("/species/{name}")
    public Specie getByName(@PathVariable String name) {
        Specie specie = specieService.getByName(name);
        if (specie == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Specie '" + name + "' not found!");
        return specie;
    }

    @GetMapping("/species/id/{id}")
    public Specie getById(@PathVariable String id) {
        Specie specie = null;
        try {
            specie = specieService.getById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot convert id: '" + id + "' into Long");
        }
        if (specie == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Specie with id '" + id + "' not found!");
        return specie;
    }

    @PostMapping("/species")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Specie addSpecie(@RequestBody Specie specie) {
        try {
            getByName(specie.getName());
        } catch (ResponseStatusException e) {
            return specieService.save(specie);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Another specie named '" + specie.getName() + "' found!");
    }

    @PutMapping("/species/{name}")
    @ResponseBody
    public Specie updateSpecie(@PathVariable String name, @RequestBody Map<String, String> parameters) {
        Specie p = getByName(name);
        try {
            if (parameters.containsKey("alimentation"))
                p.setAlimentation(Alimentation.valueOf(parameters.get("alimentation")));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'" + parameters.get("alimentation") + "' cannot be converted into Alimentation!");
        }
        if (parameters.containsKey("name"))
            p.setName(parameters.get("name"));
        try {
            if (parameters.containsKey("lifeSpan"))
                p.setLifeSpan(Short.parseShort(parameters.get("lifeSpan")));
            if (parameters.containsKey("extinctionLevel"))
                p.setExtinctionLevel(Short.parseShort(parameters.get("extinctionLevel")));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during conversion in Short: " + e.getLocalizedMessage().replace('"', '\''));
        }
        return specieService.save(p);
    }

    @DeleteMapping("/species/{name}")
    public Specie deleteSpecie(@PathVariable String name) {
        Specie specie = getByName(name);
        if (!specie.getFishList().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible to delete this specie because is referenced by some fishes. Every fish must have one specie!");
        return specieService.remove(specie);
    }
}

package fr.upem.devops.controller;

import fr.upem.devops.errors.ConflictException;
import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.Alimentation;
import fr.upem.devops.model.Fish;
import fr.upem.devops.model.Specie;
import fr.upem.devops.service.FishService;
import fr.upem.devops.service.SpecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        return specieService.getByName(name);
    }

    @PostMapping("/species")
    @ResponseBody
    public Specie addSpecie(@RequestBody Specie specie) {
        if (getByName(specie.getName()) != null)
            throw new ConflictException("Another specie named '" + specie.getName() + "' found!");
        return specieService.save(specie);
    }

    @PutMapping("/species/{name}")
    @ResponseBody
    public Specie updateSpecie(@PathVariable String name, @RequestBody Map<String, String> parameters) {
        Specie p = getByName(name);
        if (p == null) throw new ResourceNotFoundException("Specie named: '" + name + "' not found!");
        if (parameters.containsKey("alimentation"))
            p.setAlimentation(Alimentation.valueOf(parameters.get("alimentation")));
        if (parameters.containsKey("name"))
            p.setName(parameters.get("name"));
        if (parameters.containsKey("lifeSpan"))
            p.setLifeSpan(Short.parseShort(parameters.get("lifeSpan")));
        if (parameters.containsKey("extinctionLevel"))
            p.setExtinctionLevel(Short.parseShort(parameters.get("extinctionLevel")));
        if (parameters.containsKey("fishList")) {
            //TODO: fish non si aggiorna. Non aggiornare questa lista lato controller ma farlo lato angular con servizio fish
            String[] fishIds = parameters.get("fishList").split(",");
            Set<Fish> newFishList = new HashSet<>();
            for (String id : fishIds){
                Fish fish = fishService.getById(Long.parseLong(id));
                fish.setSpecie(p);
                newFishList.add(fish);
            }
            p.setFishList(newFishList);
        }
        return specieService.save(p);
    }

    @DeleteMapping("/species/{name}")
    public Specie deleteSpecie(@PathVariable String name) {
        Specie p = getByName(name);
        if (p == null) throw new ResourceNotFoundException("Specie named: '" + name + "' not found!");
        return specieService.remove(getByName(name));
    }
}

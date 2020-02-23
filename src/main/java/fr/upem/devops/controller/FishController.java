package fr.upem.devops.controller;

import fr.upem.devops.errors.ResourceNotFoundException;
import fr.upem.devops.model.Fish;
import fr.upem.devops.model.FishGender;
import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Specie;
import fr.upem.devops.service.FishService;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.SpecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

@RestController
public class FishController {

    @Autowired
    private FishService fishService;
    @Autowired
    private SpecieService specieService;
    @Autowired
    private PoolService poolService;

    @GetMapping("/fishes")
    public Iterable<Fish> getAll() {
        return fishService.getAll();
    }

    @GetMapping("/species/{specieName}/fishes")
    public Iterable<Fish> getBySpecies(@PathVariable String specieName) {
        Specie specie = this.specieService.getByName(specieName);
        if (specie == null) throw new ResourceNotFoundException("Species '" + specieName + "' not found!");
        else {
            return specie.getFishList();
        }
    }

    @GetMapping("/fishes/{id}")
    public Fish getById(@PathVariable String id) {
        Fish fish = fishService.getById(Long.parseLong(id));
        if (fish == null) throw new ResourceNotFoundException("Fish with id '" + id + "' not found!");
        return fish;
    }

    @PostMapping("/species/{specieName}/pools/{poolId}/fishes")
    @ResponseBody
    public Fish addFish(@RequestBody Fish fish, @PathVariable String specieName, @PathVariable String poolId) {
        Specie specie = this.specieService.getByName(specieName);
        if (specie == null) throw new ResourceNotFoundException("Specie '" + specieName + "' not found!");
        Pool pool = this.poolService.getById(Long.parseLong(poolId));
        if (pool == null) throw new ResourceNotFoundException("Pool '" + poolId + "' not found!");
        fish.setArrivalDate(new Date());
        fish.setSpecie(specie);
        fish.setPool(pool);
        return fishService.save(fish);
    }


    @PutMapping("/fishes/retire/{id}")
    @ResponseBody
    public Fish retireFish(@PathVariable String id) {
        Fish fishUpdated = getById(id);
        fishUpdated.setReturnDate(new Date());
        return fishService.save(fishUpdated);
    }

    @PutMapping("/fishes/{id}")
    @ResponseBody
    public Fish updateFish(@PathVariable String id, @RequestBody Map<String, String> parameters) {
        Fish p = getById(id);
        if (parameters.containsKey("name"))
            p.setName(parameters.get("name"));
        if (parameters.containsKey("gender"))
            p.setGender(FishGender.valueOf(parameters.get("gender")));
        if (parameters.containsKey("distinctSign"))
            p.setDistinctSign(parameters.get("distinctSign"));
        if (parameters.containsKey("specie") && !parameters.get("specie").isEmpty())
            p.setSpecie(specieService.getByName(parameters.get("specie")));
        if (parameters.containsKey("pool") && !parameters.get("pool").isEmpty())
            p.setPool(poolService.getById(Long.parseLong(parameters.get("pool"))));
        return fishService.save(p);
    }

    @DeleteMapping("/fishes/{id}")
    public Fish deleteFish(@PathVariable String id) {
        return fishService.remove(getById(id));
    }

}

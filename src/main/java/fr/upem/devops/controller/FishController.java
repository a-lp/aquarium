package fr.upem.devops.controller;

import fr.upem.devops.model.Fish;
import fr.upem.devops.service.FishService;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.SpecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

    @GetMapping("/fishes/{id}")
    public Fish getById(@PathVariable String id) {
        return fishService.getById(Long.parseLong(id));
    }

    @PostMapping("/fishes")
    @ResponseBody
    public Fish addFish(@RequestBody Fish fish) {
        fish.setArrivalDate(new Date());
        if (fish.getSpecie() != null) fish.setSpecie(specieService.getByName(fish.getSpecie().getName()));
        if (fish.getPool() != null) fish.setPool(poolService.getById(fish.getPool().getId()));
        return fishService.save(fish);
    }

//    @PostMapping("/fishes")
//    @ResponseBody
//    public Fish addFish(@RequestBody Map<String, String> allParams) {
//        Fish p = new Fish();
//        p.setName(allParams.get("name"));
//        p.setGender(FishGender.valueOf(allParams.get("gender")));
//        p.setDistinctSign(allParams.get("distinctSign"));
//        p.setArrivalDate(new Date());
//        p.setSpecie(specieService.getByName(allParams.get("specie")));
//
//        return fishService.save(p);
//    }

    @PutMapping("/fishes/retire/{id}")
    @ResponseBody
    public Fish retireFish(@PathVariable String id) {
        Fish fishUpdated = getById(id);
        fishUpdated.setReturnDate(new Date());
        return fishService.save(fishUpdated);
    }

    @PutMapping("/fishes/{id}")
    @ResponseBody
    public Fish updateFish(@PathVariable String id, @RequestBody Fish fish) {
        Fish p = getById(id);
        p.setName(fish.getName());
        p.setGender(fish.getGender());
        p.setDistinctSign(fish.getDistinctSign());
        p.setArrivalDate(fish.getArrivalDate());
        p.setReturnDate(fish.getReturnDate());
        p.setSpecie(fish.getSpecie());
        p.setPool(fish.getPool());
        return fishService.save(p);
    }

    @DeleteMapping("/fishes/{id}")
    public Fish deleteFish(@PathVariable String id) {
        return fishService.remove(getById(id));
    }

}

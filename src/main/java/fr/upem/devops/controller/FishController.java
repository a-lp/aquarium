package fr.upem.devops.controller;

import fr.upem.devops.model.Fish;
import fr.upem.devops.model.FishGender;
import fr.upem.devops.service.FishService;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.SpecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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

    @GetMapping("/fishes/{id}")
    public Fish getById(@PathVariable String id) {
        return fishService.getById(id);
    }

    @PostMapping("/fishes")
    @ResponseBody
    public Fish addFish(@RequestBody Fish fish) {
        fish.setArrivalDate(new Date());
        if (fish.getSpecie() != null) fish.setSpecie(specieService.getById(fish.getSpecie().getId()));
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
        Fish fishUpdated = fishService.getById(id);
        fishUpdated.setReturnDate(new Date());
        return fishService.save(fishUpdated);
    }

    @PutMapping("/fishes/{id}")
    @ResponseBody
    public Fish updateFish(@PathVariable String id, @RequestBody Map<String, String> allParams) {
        Fish p = fishService.getById(id);
        p.setName(allParams.get("name"));
        p.setGender(FishGender.valueOf(allParams.get("gender")));
        p.setDistinctSign(allParams.get("distinctSign"));
//        p.setArrivalDate(Date.valueOf(allParams.get("arrivalDate")));
//        p.setReturnDate(Date.valueOf(allParams.get("returnDate")));
//        p.setSpecie(allParams.get("specie"));
        return fishService.save(p);
    }

    @DeleteMapping("/fishes/{id}")
    public Fish deleteFish(@PathVariable String id) {
        return fishService.remove(fishService.getById(id));
    }

}

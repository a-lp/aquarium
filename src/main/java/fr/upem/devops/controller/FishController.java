package fr.upem.devops.controller;

import fr.upem.devops.model.Fish;
import fr.upem.devops.model.FishGender;
import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Specie;
import fr.upem.devops.service.FishService;
import fr.upem.devops.service.PoolService;
import fr.upem.devops.service.SpecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/species/{specieName}/fishes")
    public Iterable<Fish> getBySpecies(@PathVariable String specieName) {
        Specie specie = getSpecie(specieName);
        return specie.getFishList();
    }


    @GetMapping("/fishes/{id}")
    public Fish getById(@PathVariable String id) {
        Fish fish = null;
        try {
            fish = fishService.getById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot convert id: '" + id + "' into Long");
        }
        if (fish == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fish with id: '" + id + "' not found!");
        return fish;
    }

    @PostMapping("/species/{specieName}/pools/{poolId}/fishes")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Fish addFish(@RequestBody Fish fish, @PathVariable String specieName, @PathVariable String poolId) throws ResponseStatusException {
        Specie specie = getSpecie(specieName);
        Pool pool = getPool(poolId);
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
        if (parameters.containsKey("distinctSign"))
            p.setDistinctSign(parameters.get("distinctSign"));
        try {
            if (parameters.containsKey("gender"))
                p.setGender(FishGender.valueOf(parameters.get("gender")));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage().replace('"', '\''));
        }
        if (parameters.containsKey("specie"))
            p.setSpecie(getSpecie(parameters.get("specie")));
        if (parameters.containsKey("pool")) {
            Pool pool = getPool(parameters.get("pool"));
            p.setPool(pool);
        }
        return fishService.save(p);
    }

    @DeleteMapping("/fishes/{id}")
    public Fish deleteFish(@PathVariable String id) {
        return fishService.remove(getById(id));
    }

//    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error during conversione of the input request!")
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    @ResponseBody
//    void onHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
//    }

    private Specie getSpecie(String specieName) {
        Specie specie = this.specieService.getByName(specieName);
        if (specie == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Specie '" + specieName + "' not found!");
        return specie;
    }

    private Pool getPool(String id) {
        Pool pool = null;
        try {
            pool = this.poolService.getById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot convert pool id: '" + id + "' into Long");
        }
        if (pool == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pool with id '" + id + "' not found!");
        return pool;
    }
}

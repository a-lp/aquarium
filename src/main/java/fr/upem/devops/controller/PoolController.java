package fr.upem.devops.controller;

import fr.upem.devops.model.Fish;
import fr.upem.devops.model.Pool;
import fr.upem.devops.service.PoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PoolController {
    @Autowired
    private PoolService poolService;

    @GetMapping("/pools")
    public Iterable<Pool> getAll() {
        return poolService.getAll();
    }

    @GetMapping("/pools/{id}")
    public Pool getById(@PathVariable String id) {
        return poolService.getById(Long.parseLong(id));
    }

    @PostMapping("/pools")
    @ResponseBody
    public Pool addPool(@RequestBody Pool pool) {
        return poolService.save(pool);
    }

    @PostMapping("/pools/{id}")
    @ResponseBody
    public Pool addFishToPool(@RequestBody Fish fish, @PathVariable String id) {
        Pool pool = getById(id);
        if (pool == null) return null;
        pool.addFish(fish);
        return poolService.save(pool);
    }
}

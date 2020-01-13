package fr.upem.devops.controller;

import fr.upem.devops.model.Animal;
import fr.upem.devops.model.AnimalGender;
import fr.upem.devops.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Map;

@RestController
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @GetMapping("/animals")
    public Iterable<Animal> getAll() {
        return animalService.getAll();
    }

    @GetMapping("/animals/{id}")
    public Animal getById(@PathVariable String id) {
        return animalService.getById(id);
    }

    @PostMapping("/animals")
    @ResponseBody
    public Animal addAnimal(@RequestBody Animal Animal) {
        return animalService.save(Animal);
    }

    @PutMapping("/animals/{id}")
    @ResponseBody
    public Animal updateAnimal(@PathVariable String id, @RequestBody Map<String, String> allParams) {
        Animal p = animalService.getById(id);
        p.setName(allParams.get("name"));
        p.setGender(AnimalGender.valueOf(allParams.get("gender")));
        p.setDistinctSign(allParams.get("distinctSign"));
//        p.setArrivalDate(Date.valueOf(allParams.get("arrivalDate")));
//        p.setReturnDate(Date.valueOf(allParams.get("returnDate")));
        return animalService.save(p);
    }

    @DeleteMapping("/animals/{id}")
    public Animal deleteAnimal(@PathVariable String id) {
        return animalService.remove(animalService.getById(id));
    }

}

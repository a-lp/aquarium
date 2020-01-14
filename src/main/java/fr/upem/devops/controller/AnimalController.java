package fr.upem.devops.controller;

import fr.upem.devops.model.Animal;
import fr.upem.devops.model.AnimalGender;
import fr.upem.devops.service.AnimalService;
import fr.upem.devops.service.SpecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
public class AnimalController {

    @Autowired
    private AnimalService animalService;
    @Autowired
    private SpecieService specieService;

    @GetMapping("/animals")
    public Iterable<Animal> getAll() {
        return animalService.getAll();
    }

    @GetMapping("/animals/{id}")
    public Animal getById(@PathVariable String id) {
        return animalService.getById(id);
    }

//    @PostMapping("/animals")
//    @ResponseBody
//    public Animal addAnimal(@RequestBody Animal animal) {
//        animal.setArrivalDate(new Date());
//        System.out.println(animal);
//        return animalService.save(animal);
//    }

    @PostMapping("/animals")
    @ResponseBody
    public Animal addAnimal(@RequestBody Map<String, String> allParams) {
        Animal p = new Animal();
        p.setName(allParams.get("name"));
        p.setGender(AnimalGender.valueOf(allParams.get("gender")));
        p.setDistinctSign(allParams.get("distinctSign"));
        p.setArrivalDate(new Date());
        p.setSpecie(specieService.getByName(allParams.get("specie")));
        return animalService.save(p);
    }

    @PutMapping("/animals/retire/{id}")
    @ResponseBody
    public Animal retireAnimal(@PathVariable String id) {
        Animal animalUpdated = animalService.getById(id);
        animalUpdated.setReturnDate(new Date());
        return animalService.save(animalUpdated);
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
//        p.setSpecie(allParams.get("specie"));
        return animalService.save(p);
    }

    @DeleteMapping("/animals/{id}")
    public Animal deleteAnimal(@PathVariable String id) {
        return animalService.remove(animalService.getById(id));
    }

}

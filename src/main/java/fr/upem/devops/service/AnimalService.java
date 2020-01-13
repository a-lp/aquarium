package fr.upem.devops.service;

import fr.upem.devops.model.Animal;
import fr.upem.devops.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalService {
    @Autowired
    private AnimalRepository animalRepository;

    public Iterable<Animal> getAll() {

        return animalRepository.findAll();
    }

    public Animal save(Animal animal) {
        return animalRepository.save(animal);
    }

    public void update(Animal animal) {
        animalRepository.save(animal);
    }

    public Animal getById(String id) {
        return animalRepository.findById(Long.parseLong(id)).get();
    }

    public Animal remove(Animal animal) {
        animalRepository.delete(animal);
        return animal;
    }
}

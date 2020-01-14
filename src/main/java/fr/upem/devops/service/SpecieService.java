package fr.upem.devops.service;

import fr.upem.devops.model.Specie;
import fr.upem.devops.repository.SpecieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecieService {
    @Autowired
    private SpecieRepository specieRepository;

    public Iterable<Specie> getAll() {
        return specieRepository.findAll();
    }

    public Specie save(Specie specie) {
        return specieRepository.save(specie);
    }

    public void update(Specie specie) {
        specieRepository.save(specie);
    }

    public Specie getByName(String name) {
        for (Specie specie : specieRepository.findAll()) {
            if (specie.getName().equals(name)) return specie;
        }
        return null;
    }

    public Specie remove(Specie specie) {
        specieRepository.delete(specie);
        return specie;
    }
}

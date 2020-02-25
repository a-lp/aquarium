package fr.upem.devops.service;

import fr.upem.devops.model.Fish;
import fr.upem.devops.model.Pool;
import fr.upem.devops.model.Sector;
import fr.upem.devops.model.Staff;
import fr.upem.devops.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SectorService {
    @Autowired
    private SectorRepository sectorRepository;

    public Iterable<Sector> getAll() {
        return sectorRepository.findAll();
    }

    public Sector getById(Long id) {
        return sectorRepository.findById(id).orElse(null);
    }

    public Sector save(Sector specie) {
        return sectorRepository.save(specie);
    }

    public void update(Sector specie) {
        sectorRepository.save(specie);
    }

    public Sector getByName(String name) {
        for (Sector specie : sectorRepository.findAll()) {
            if (specie.getName().equals(name)) return specie;
        }
        return null;
    }

    public Sector remove(Sector specie) {
        sectorRepository.delete(specie);
        return specie;
    }
}

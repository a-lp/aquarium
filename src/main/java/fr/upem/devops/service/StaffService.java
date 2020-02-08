package fr.upem.devops.service;

import fr.upem.devops.model.Staff;
import fr.upem.devops.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    public Iterable<Staff> getAll() {

        return staffRepository.findAll();
    }

    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }


    public void update(Staff staff) {
        staffRepository.save(staff);
    }

    public Staff getById(Long id) {
        return staffRepository.findById(id).orElse(null);
    }

    public Staff remove(Staff staff) {
        staffRepository.delete(staff);
        return staff;
    }
}

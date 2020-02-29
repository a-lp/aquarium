package fr.upem.devops.service;

import fr.upem.devops.model.Sector;
import fr.upem.devops.model.Staff;
import fr.upem.devops.model.User;
import fr.upem.devops.repository.StaffRepository;
import fr.upem.devops.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private UserRepository userRepository;

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
        staff.setCredentials(null);
        staffRepository.delete(staff);
        staff.setActivities(new HashSet<>());
        staff.setPoolsResponsabilities(new ArrayList<>());
        return staff;
    }

    //TODO: TO REMOVE
    public Iterable<User> getProfiles() {
        return userRepository.findAll();
    }

    public Iterable<Staff> getByRole(Staff.StaffRole role) {
        return staffRepository.findByRole(role);
    }
}

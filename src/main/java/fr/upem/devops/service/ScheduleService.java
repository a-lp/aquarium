package fr.upem.devops.service;

import fr.upem.devops.model.Schedule;
import fr.upem.devops.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    public Iterable<Schedule> getAll() {

        return scheduleRepository.findAll();
    }

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }


    public void update(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public Schedule getById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    public Schedule remove(Schedule schedule) {
        schedule.getPool().removeSchedule(schedule);
        scheduleRepository.delete(schedule);
        return schedule;
    }
}

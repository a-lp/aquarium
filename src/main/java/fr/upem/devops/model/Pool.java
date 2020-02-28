package fr.upem.devops.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Pool.class)
public class Pool implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long maxCapacity;
    private Double volume;
    @Enumerated(EnumType.STRING)
    private WaterCondition condition;
    @OneToMany(mappedBy = "pool")
    @JsonIdentityReference(alwaysAsId=true)
    private Set<Fish> fishes = new HashSet<>();
    @OneToMany(mappedBy = "pool", cascade = CascadeType.REMOVE)
    @JsonIdentityReference(alwaysAsId=true)
    private Set<Schedule> schedules = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "pool_sector")
    @JsonIdentityReference(alwaysAsId=true)
    private Sector sector;
    @ManyToOne
    @JoinColumn(name = "pool_responsible")
    @JsonIdentityReference(alwaysAsId=true)
    private Staff responsible;

    public Pool() {
    }

    public Pool(Long id, Long maxCapacity, Double volume, WaterCondition waterCondition, Set<Fish> fishes) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.volume = volume;
        this.condition = waterCondition;
        this.fishes = fishes;
    }

    public Pool(Long maxCapacity, Double volume, WaterCondition waterCondition, Set<Fish> fishes) {
        this.maxCapacity = maxCapacity;
        this.volume = volume;
        this.condition = waterCondition;
        this.fishes = fishes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Long maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public WaterCondition getCondition() {
        return condition;
    }

    public void setCondition(WaterCondition condition) {
        this.condition = condition;
    }

    public Set<Fish> getFishes() {
        return fishes;
    }

    public void setFishes(Set<Fish> fish) {
        this.fishes = fish;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Staff getResponsible() {
        return responsible;
    }

    public void setResponsible(Staff responsible) {
        this.responsible = responsible;
    }

    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<Schedule> scheduledActivities) {
        this.schedules = scheduledActivities;
    }


    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        Pool other = (Pool) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Pool{" +
                "id=" + id +
                ", maxCapacity=" + maxCapacity +
                ", volume=" + volume +
                ", condition=" + condition +
                ", sector=" + (sector == null ? "null" : sector.getName()) +
                ", responsible=" + (responsible == null ? "null" : responsible.getId()) +
                '}';
    }

    public void addFish(Fish fish) {
        this.fishes.add(fish);
    }

    public void removeSchedule(Schedule schedule) {
        this.schedules.remove(schedule);
    }


    public enum WaterCondition implements Serializable {
        CLEAN, DIRTY
    }
}

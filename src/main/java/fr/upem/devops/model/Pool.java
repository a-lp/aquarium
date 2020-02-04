package fr.upem.devops.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pool implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long maxCapacity;
    private Double volume;
    @Enumerated(EnumType.STRING)
    private WaterCondition condition;
    @OneToMany(mappedBy = "pool")
    @JsonManagedReference(value = "fish-pool")
    private List<Fish> fishes = new ArrayList<>();

    public Pool() {
    }

    public Pool(Long id, Long maxCapacity, Double volume, WaterCondition waterCondition, List<Fish> fishes) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.volume = volume;
        this.condition = waterCondition;
        this.fishes = fishes;
    }

    public Pool(Long maxCapacity, Double volume, WaterCondition waterCondition, List<Fish> fishes) {
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

    public List<Fish> getFishes() {
        return fishes;
    }

    public void setFishes(List<Fish> fish) {
        this.fishes = fish;
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
                ", fish=" + fishes +
                '}';
    }

    public enum WaterCondition implements Serializable {
        CLEAN, DIRTY
    }
}

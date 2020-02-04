package fr.upem.devops.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pool {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long maxCapacity;
    private Double volume;
    @Enumerated(EnumType.STRING)
    private WaterCondition condition;
//    @OneToMany(mappedBy = "pool")
//    @JsonManagedReference
//    private List<Fish> fish = new ArrayList<>();

    public Pool() {
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
//
//    public List<Fish> getFish() {
//        return fish;
//    }
//
//    public void setFish(List<Fish> fish) {
//        this.fish = fish;
//    }

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

    public enum WaterCondition implements Serializable {
        CLEAN, DIRTY
    }
}

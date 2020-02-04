package fr.upem.devops.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Specie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name", unique = true)
    private String name;
    private Short lifeSpan;
    private Short extinctionLevel;
    @Enumerated(EnumType.STRING)
    private Alimentation alimentation;
    @OneToMany(mappedBy = "specie")
    @JsonManagedReference(value = "fish-specie")
    private List<Fish> fishList = new ArrayList<>();

    public Specie() {
    }

    public Specie(String name, Short lifeSpan, Short exctintionLevel, Alimentation alimentation, ArrayList<Fish> fishList) {
        this.name = name;
        this.lifeSpan = lifeSpan;
        this.alimentation = alimentation;
        this.extinctionLevel = exctintionLevel;
        this.fishList = fishList;
    }

    public Specie(Long id, String name, Short lifeSpan, Short exctintionLevel, Alimentation alimentation, ArrayList<Fish> fishList) {
        this.id = id;
        this.name = name;
        this.lifeSpan = lifeSpan;
        this.alimentation = alimentation;
        this.extinctionLevel = exctintionLevel;
        this.fishList = fishList;
    }

    public void addAnimal(Fish fish) {
        this.fishList.add(fish);
    }

    public Short getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(Short lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public Short getExtinctionLevel() {
        return extinctionLevel;
    }

    public void setExtinctionLevel(Short extinctionLevel) {
        this.extinctionLevel = extinctionLevel;
    }

    public Alimentation getAlimentation() {
        return alimentation;
    }

    public void setAlimentation(Alimentation alimentation) {
        this.alimentation = alimentation;
    }

    public String getName() {
        return name;
    }

    public List<Fish> getFishList() {
        return fishList;
    }

    public void setFishList(List<Fish> fishList) {
        this.fishList = fishList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specie other = (Specie) o;
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
        return "Specie{" +
                "name='" + name + '\'' +
                ", lifeSpan=" + lifeSpan +
                ", exctintionLevel=" + extinctionLevel +
                ", alimentation=" + alimentation +
                '}';
    }

    public Long getId() {
        return this.id;
    }
}

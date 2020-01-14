package fr.upem.devops.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Specie implements Serializable {
    @Id
    @Column(unique = true)
    private String name;
    private Short lifeSpan;
    private Short extinctionLevel;
    @Enumerated(EnumType.STRING)
    private Alimentation alimentation;
    @OneToMany(mappedBy = "specie")
    @JsonManagedReference
    private List<Animal> animalList = new ArrayList<>();

    public Specie() {
    }

    public Specie(String name, Short lifeSpan, Alimentation alimentation, Short exctintionLevel) {
        this.name = name;
        this.lifeSpan = lifeSpan;
        this.alimentation = alimentation;
        this.extinctionLevel = exctintionLevel;
    }

    public void addAnimal(Animal animal) {
        this.animalList.add(animal);
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

    public List<Animal> getAnimalList() {
        return animalList;
    }

    public void setAnimalList(List<Animal> animalList) {
        this.animalList = animalList;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        Specie other = (Specie) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
}

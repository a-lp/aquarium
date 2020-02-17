package fr.upem.devops.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Fish.class)
public class Fish implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private FishGender gender;
    private String distinctSign;
    private Date arrivalDate;
    private Date returnDate;
    @ManyToOne
    @JoinColumn(name = "fish_specie_id")
    private Specie specie;
    @ManyToOne
    @JoinColumn(name = "fish_pool_id")
    private Pool pool;

    public Fish() {
    }

    /* Test constructors */
    public Fish(String name, FishGender gender, String distinctSign, Specie specie, Pool pool) {
        this.name = name;
        this.gender = gender;
        this.distinctSign = distinctSign;
        this.specie = specie;
        this.pool = pool;
    }

    public Fish(Long id, String name, FishGender gender, String distinctSign, Specie specie, Pool pool) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.distinctSign = distinctSign;
        this.specie = specie;
        this.pool = pool;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public Specie getSpecie() {
        return specie;
    }

    public void setSpecie(Specie specie) {
        this.specie = specie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FishGender getGender() {
        return gender;
    }

    public void setGender(FishGender gender) {
        this.gender = gender;
    }

    public String getDistinctSign() {
        return distinctSign;
    }

    public void setDistinctSign(String distinctSign) {
        this.distinctSign = distinctSign;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        Fish other = (Fish) obj;
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
        return "Fish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", distinctSign='" + distinctSign + '\'' +
                ", arrivalDate=" + arrivalDate +
                ", returnDate=" + returnDate +
                ", specie=" + (specie != null ? specie.getName() : "null") +
                ", pool=" + (pool != null ? pool.getId() : "null") +
                '}';
    }
}

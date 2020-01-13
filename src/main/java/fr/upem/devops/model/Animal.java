package fr.upem.devops.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private AnimalGender gender;
    private String distinctSign;
    private Date arrivalDate;
    private Date returnDate;

    public Animal() {
    }

    public Animal(String name, AnimalGender gender, String distinctSign) {
        this.name = name;
        this.gender = gender;
        this.distinctSign = distinctSign;
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

    public AnimalGender getGender() {
        return gender;
    }

    public void setGender(AnimalGender gender) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id.equals(animal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", distinctSign='" + distinctSign + '\'' +
                ", arrivalDate=" + arrivalDate +
                ", returnDate=" + returnDate +
                '}';
    }

}

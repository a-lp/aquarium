package fr.upem.devops.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name", scope = Sector.class)
public class Sector implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    @OneToMany(mappedBy = "sector", cascade = CascadeType.REMOVE)
    @JsonIdentityReference(alwaysAsId=true)
    private Set<Pool> pools = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "sectors_staffs",
            joinColumns = @JoinColumn(name = "staff_id"),
            inverseJoinColumns = @JoinColumn(name = "sector_id")
    )
    @JsonIdentityReference(alwaysAsId=true)
    private Set<Staff> staffList = new HashSet<>();

    public Sector() {
    }

    public Sector(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Sector(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Sector(Long id, String name, String location, Set<Pool> pools) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.pools = pools;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Pool> getPools() {
        return pools;
    }

    public void setPools(Set<Pool> pools) {
        this.pools = pools;
    }

    public void addPool(Pool pool) {
        this.pools.add(pool);
    }

    public Set<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(Set<Staff> staffList) {
        this.staffList = staffList;
    }

    public void assignStaff(Staff staff) {
        this.staffList.add(staff);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sector other = (Sector) o;
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
        return "Sector{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", pools=" + pools +
                '}';
    }

    public void removePool(Pool pool) {
        this.pools.remove(pool);
    }

    public void removeStaff(Staff staff) {
        this.staffList.remove(staff);
    }
}

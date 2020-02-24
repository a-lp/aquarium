package fr.upem.devops.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Staff.class)
public class Staff implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String address;
    private Date birthday;
    private String socialSecurity;
    private StaffRole role;
    @OneToMany(mappedBy = "responsible")
    @JsonIdentityReference(alwaysAsId = true)
    private List<Pool> poolsResponsabilities = new ArrayList<>();
    @ManyToMany(mappedBy = "staffList")
    @JsonIdentityReference(alwaysAsId = true)
    private List<Sector> sectors = new ArrayList<>();
    @ManyToMany
    @JsonIdentityReference(alwaysAsId = true)
    private Set<PoolActivity> activities = new HashSet<>();

    public Staff() {
    }

    public Staff(Long id, String name, String surname, String address, Date birthday, String socialSecurity, StaffRole role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.birthday = birthday;
        this.socialSecurity = socialSecurity;
        this.role = role;
    }

    public Staff(String name, String surname, String address, Date birthday, String socialSecurity, StaffRole role) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.birthday = birthday;
        this.socialSecurity = socialSecurity;
        this.role = role;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSocialSecurity() {
        return socialSecurity;
    }

    public void setSocialSecurity(String socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public List<Pool> getPoolsResponsabilities() {
        return poolsResponsabilities;
    }

    public void setPoolsResponsabilities(List<Pool> responsabilities) {
        this.poolsResponsabilities = responsabilities;
    }

    public List<Sector> getSectors() {
        return sectors;
    }

    public void setSectors(List<Sector> sectors) {
        this.sectors = sectors;
    }

    public void assignPool(Pool pool) {
        this.poolsResponsabilities.add(pool);
    }

    public void assignSector(Sector sector) {
        this.sectors.add(sector);
    }

    public StaffRole getRole() {
        return role;
    }

    public void setRole(StaffRole role) {
        this.role = role;
    }

    public Set<PoolActivity> getActivities() {
        return activities;
    }

    public void setActivities(Set<PoolActivity> activities) {
        this.activities = activities;
    }

    public void assignActivity(PoolActivity activity) {
        this.activities.add(activity);
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        Staff other = (Staff) obj;
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

    public void removePoolResponsability(Pool pool) {
        this.poolsResponsabilities.remove(pool);
    }

    public void removeStaff(Staff staff) {
        this.sectors.remove(staff);
    }

    public void removeActivity(PoolActivity poolActivity) {
        this.activities.remove(poolActivity);
    }

    public void removeSector(Sector sector) {
        this.sectors.remove(sector);
    }

    public enum StaffRole {
        ADMIN, MANAGER, WORKER
    }
}

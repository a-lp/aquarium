package fr.upem.devops.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = PoolActivity.class)
public class PoolActivity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private LocalTime startActivity;
    private LocalTime endActivity;
    private Date day;
    private Boolean openToPublic = false;
    private Boolean repeated = false;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "activity_staff",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id")
    )
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Staff> staffList = new HashSet<>();
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Schedule schedule;

    public PoolActivity() {
    }

    public PoolActivity(Long id, LocalTime startActivity, LocalTime endActivity, Boolean openToPublic, Set<Staff> staffList, Schedule schedule) {
        this.id = id;
        this.startActivity = startActivity;
        this.endActivity = endActivity;
        this.openToPublic = openToPublic;
        this.staffList = staffList;
        this.schedule = schedule;
    }

    public PoolActivity(LocalTime startActivity, LocalTime endActivity, Boolean openToPublic, Set<Staff> staffList, Schedule schedule) {
        this.startActivity = startActivity;
        this.endActivity = endActivity;
        this.openToPublic = openToPublic;
        this.staffList = staffList;
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getStartActivity() {
        return startActivity;
    }

    public void setStartActivity(LocalTime startActivity) {
        this.startActivity = startActivity;
    }

    public LocalTime getEndActivity() {
        return endActivity;
    }

    public void setEndActivity(LocalTime endActivity) {
        this.endActivity = endActivity;
    }

    public Set<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(Set<Staff> staffList) {
        this.staffList = staffList;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Boolean getRepeated() {
        return repeated;
    }

    public void setRepeated(Boolean repeated) {
        this.repeated = repeated;
    }

    public void assignStaff(Staff staff) {
        this.staffList.add(staff);
    }

    public Boolean getOpenToPublic() {
        return openToPublic;
    }

    public void setOpenToPublic(Boolean openToPublic) {
        this.openToPublic = openToPublic;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        PoolActivity other = (PoolActivity) obj;
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
        return "PoolActivity{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", startActivity=" + startActivity +
                ", endActivity=" + endActivity +
                ", openToPublic=" + openToPublic +
                ", day=" + day +
                ", schedule=" + (schedule == null ? "null" : schedule.getId()) +
                '}';
    }

    public void removeStaff(Staff staff) {
        this.staffList.remove(staff);
    }
}

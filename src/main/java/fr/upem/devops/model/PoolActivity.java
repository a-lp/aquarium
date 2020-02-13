package fr.upem.devops.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class PoolActivity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Date startActivity;
    private Date endActivity;
    private Boolean openToPublic;
    private Boolean repeated;
    @ManyToMany
    @JoinTable(
            name = "activity_staff",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id")
    )
    private List<Staff> staffList = new ArrayList<>();
    @ManyToOne
    private Schedule schedule;

    public PoolActivity() {
    }

    public PoolActivity(Long id, Date startActivity, Date endActivity, Boolean openToPublic, List<Staff> staffList, Schedule schedule) {
        this.id = id;
        this.startActivity = startActivity;
        this.endActivity = endActivity;
        this.openToPublic = openToPublic;
        this.staffList = staffList;
        this.schedule = schedule;
    }

    public PoolActivity(Date startActivity, Date endActivity, Boolean openToPublic, List<Staff> staffList, Schedule schedule) {
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

    public Date getStartActivity() {
        return startActivity;
    }

    public void setStartActivity(Date startActivity) {
        this.startActivity = startActivity;
    }

    public Date getEndActivity() {
        return endActivity;
    }

    public void setEndActivity(Date endActivity) {
        this.endActivity = endActivity;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
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
                ", schedule=" + (schedule == null ? "null" : schedule.getId()) +
                '}';
    }
}

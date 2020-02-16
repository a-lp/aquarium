package fr.upem.devops.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Schedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date startPeriod;
    private Date endPeriod;
    @ManyToOne
    @JoinColumn(name = "calendar_pool")
    private Pool pool;
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<PoolActivity> scheduledActivities = new ArrayList<>();

    public Schedule() {
    }

    public Schedule(Long id, Date startPeriod, Date endPeriod, List<PoolActivity> scheduledActivities) {
        this.id = id;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.scheduledActivities = scheduledActivities;
    }

    public Schedule(Date startPeriod, Date endPeriod, List<PoolActivity> scheduledActivities) {
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.scheduledActivities = scheduledActivities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(Date startPeriod) {
        this.startPeriod = startPeriod;
    }

    public Date getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(Date endPeriod) {
        this.endPeriod = endPeriod;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public List<PoolActivity> getScheduledActivities() {
        return scheduledActivities;
    }

    public void setScheduledActivities(List<PoolActivity> activities) {
        this.scheduledActivities = activities;
    }

    public void assignActivity(PoolActivity activity) {
        this.scheduledActivities.add(activity);
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        Schedule other = (Schedule) obj;
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
}

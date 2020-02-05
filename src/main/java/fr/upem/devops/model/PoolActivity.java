package fr.upem.devops.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class PoolActivity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private Date startActivity;
    private Date endActivity;
    private Date day;
//    @ManyToMany
//    @JoinTable(
//            name = "pool_activities",
//            joinColumns = @JoinColumn(name = "activity_id"),
//            inverseJoinColumns = @JoinColumn(name = "pool_id")
//    )
//    private List<Pool> pools = new ArrayList<>();

    public PoolActivity() {
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

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }
//
//    public List<Pool> getPools() {
//        return pools;
//    }
//
//    public void setPools(List<Pool> pools) {
//        this.pools = pools;
//    }

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
                ", day=" + day +
//                ", pools=" + pools +
                '}';
    }
}

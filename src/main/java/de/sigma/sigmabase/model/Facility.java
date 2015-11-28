package de.sigma.sigmabase.model;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by:  nilsraabe
 * Date:        28.11.15
 * Time:        22:59
 * E-Mail:      satriani.vay@gmail.com
 */
@Entity
@Table(name = "t_facility", uniqueConstraints = {@UniqueConstraint(columnNames = "id"), @UniqueConstraint(columnNames = "name")})
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Version
    private Long version;

    @LastModifiedDate
    @Column(nullable = false, unique = false)
    private DateTime lastmodified;

    @CreatedDate
    @Column(nullable = false, unique = false)
    private DateTime creationdate;

    @Column(nullable = false, unique = true, length = 200)
    private String name;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @PrePersist
    void onCreate() {
        setCreationdate(new DateTime());
        setLastmodified(new DateTime());
    }

    @PreUpdate
    void onPersist() {
        setLastmodified(new DateTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Facility)) return false;
        Facility facility = (Facility) o;
        return Objects.equals(id, facility.id) &&
                Objects.equals(version, facility.version) &&
                Objects.equals(lastmodified, facility.lastmodified) &&
                Objects.equals(creationdate, facility.creationdate) &&
                Objects.equals(name, facility.name) &&
                Objects.equals(latitude, facility.latitude) &&
                Objects.equals(longitude, facility.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, lastmodified, creationdate, name, latitude, longitude);
    }

    @Override
    public String toString() {
        return "Facility{" +
                "id=" + id +
                ", version=" + version +
                ", lastmodified=" + lastmodified +
                ", creationdate=" + creationdate +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

        /*
    ############# Getter & Setter #############
     */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public DateTime getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(DateTime lastmodified) {
        this.lastmodified = lastmodified;
    }

    public DateTime getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(DateTime creationdate) {
        this.creationdate = creationdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

package de.sigma.sigmabase.model.user;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by:  nilsraabe
 * Date:        29.11.15
 * Time:        18:41
 * E-Mail:      satriani.vay@gmail.com
 */
@Entity
@Table(name = "t_registrationkeys", uniqueConstraints = {@UniqueConstraint(columnNames = "id"), @UniqueConstraint(columnNames = "key")})
public class RegistrationKey {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @LastModifiedDate
    @Column(nullable = false, unique = false)
    private DateTime lastmodified;

    @CreatedDate
    @Column(nullable = false, unique = false)
    private DateTime creationdate;

    @Column(nullable = false, length = 1024)
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    private User user;

    @Override
    public String toString() {
        return "RegistrationKey{" +
                "userRole=" + userRole +
                ", key='" + key + '\'' +
                ", creationdate=" + creationdate +
                ", lastmodified=" + lastmodified +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistrationKey)) return false;
        RegistrationKey that = (RegistrationKey) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(lastmodified, that.lastmodified) &&
                Objects.equals(creationdate, that.creationdate) &&
                Objects.equals(key, that.key) &&
                Objects.equals(userRole, that.userRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastmodified, creationdate, key, userRole);
    }

    @PrePersist
    void onCreate() {
        setCreationdate(new DateTime());
        setLastmodified(new DateTime());
    }

    @PreUpdate
    void onPersist() {
        setLastmodified(new DateTime());
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

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}

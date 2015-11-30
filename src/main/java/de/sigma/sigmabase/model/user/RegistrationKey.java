package de.sigma.sigmabase.model.user;

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

    @Column(nullable = false, length = 1024)
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @OneToOne(mappedBy = "registrationKey", cascade = CascadeType.ALL)
    private User user;

    @Override
    public String toString() {
        return "RegistrationKey{" +
                "id=" + id +
                ", userRole=" + userRole +
                ", key='" + key + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistrationKey)) return false;
        RegistrationKey that = (RegistrationKey) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userRole, that.userRole) &&
                Objects.equals(key, that.key) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userRole, key, user);
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
}

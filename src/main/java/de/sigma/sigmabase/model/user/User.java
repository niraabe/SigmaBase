package de.sigma.sigmabase.model.user;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Objects;

/**
 * This class holds a User of ravenchirp
 * <p/>
 * Created by:  nilsraabe
 * Date:        07.06.15
 * Time:        21:47
 * E-Mail:      satriani.vay@gmail.com
 */
@Entity
@Table(name = "t_user", uniqueConstraints = {@UniqueConstraint(columnNames = "id"), @UniqueConstraint(columnNames = "username")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Version
    private Long version;

    @LastModifiedDate
    @Column(nullable = false, unique = false)
    private DateTime lastmodified;

    @CreatedDate
    @Column(nullable = false, unique = false)
    private DateTime creationdate;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String forename;

    @Column(nullable = false, length = 50)
    private String surname;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd.MM.yyyy")
    private DateTime birthday;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 1024)
    private String description;

    @OneToOne
    @JoinColumn(name = "registrsationkey")
    private RegistrationKey registrationKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private UserRole userRole;

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
    public String toString() {
        return "User{" +
                "id=" + id +
                ", version=" + version +
                ", lastmodified=" + lastmodified +
                ", creationdate=" + creationdate +
                ", username='" + username + '\'' +
                ", forename='" + forename + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday=" + birthday +
                ", gender=" + gender +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", registrationKey=" + registrationKey +
                ", userRole=" + userRole +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(version, user.version) &&
                Objects.equals(lastmodified, user.lastmodified) &&
                Objects.equals(creationdate, user.creationdate) &&
                Objects.equals(username, user.username) &&
                Objects.equals(forename, user.forename) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(birthday, user.birthday) &&
                Objects.equals(gender, user.gender) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(description, user.description) &&
                Objects.equals(registrationKey, user.registrationKey) &&
                Objects.equals(userRole, user.userRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, lastmodified, creationdate, username, forename, surname, birthday, gender, password, email,
                description, registrationKey, userRole);
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public DateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(DateTime birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormattedBirthday() {
        DateTimeFormatter dtf = org.joda.time.format.DateTimeFormat.forPattern("dd.MM.yyyy");
        return birthday.toString(dtf);
    }

    public RegistrationKey getRegistrationKey() {
        return registrationKey;
    }

    public void setRegistrationKey(RegistrationKey registrationKey) {
        this.registrationKey = registrationKey;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}


package oleg.klimov.SubscriberLifecycle.entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name="subscribers")
public class Subscriber implements UserDetails {
    @Id
    @Column(name = "msisdn")
    @JsonView(Views.Subscriber.class)
    private Long msisdn;
    @Column(name = "msisdn", insertable = false, updatable = false)
    private String username;
    @JsonView(Views.Subscriber.class)
    private String name;
    @JsonView(Views.Subscriber.class)
    private String surname;
    private String password;
    @JsonView(Views.Subscriber.class)
    private Float balance;
    private Boolean enabled;
    @JsonView(Views.Subscriber.class)
    private Boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name="subscribers_role",joinColumns = @JoinColumn(name = "subscribers_msisdn"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rate_id", referencedColumnName = "id")
    private Rate rate;

    public Subscriber() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled=enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Long getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(Long msisdn) {
        this.msisdn = msisdn;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }
}

package se.sakerhet.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    // Return an empty list for now (no roles configured)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }


    @Override
    public String getUsername() {
        return email;
    }

    //Todo keep?
/*
    @Override
    public boolean isAccountNonExpired() {
        return true; // Adjust logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Adjust logic if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Adjust logic if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Adjust logic if needed
    }*/
}

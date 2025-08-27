package com.ftn.ts.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@ToString
@Getter
@Setter
@Entity
@Table(name="od_users")
public class UserOD implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique=true)
    private String email;
    private String password;
    private String name;
    private String surname;
    @Lob
    @Column(length=1000000)
    @Nullable
    private byte[] picture;
    private boolean activated;
    @Nullable
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;
    private String address;
    private String phone;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

}

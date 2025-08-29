package com.ftn.ts.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "user_od")
public class UserOD extends BaseUser{
    private String surname;
    private boolean activated;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Picture picture;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER_OD"));
    }
}

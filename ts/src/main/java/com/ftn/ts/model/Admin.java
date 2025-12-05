package com.ftn.ts.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
public class Admin extends BaseUser{

    private boolean unlocked;

    public Admin() { unlocked = false; }

    public void unlock() {
        this.unlocked = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}

package com.ftn.ts.service;

import com.ftn.ts.dto.UserODDTO;
import com.ftn.ts.model.UserOD;
import com.ftn.ts.repository.UserODRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserODService implements UserDetailsService {
    @Autowired
    UserODRepository userODRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userODRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException(username));

    }

    public UserOD getUserByEmail(String name) {
        return null;
    }

    public Object newUser(UserODDTO dto) {
        return null;
    }

    public void activateUser(String newUserMail) {
    }
}

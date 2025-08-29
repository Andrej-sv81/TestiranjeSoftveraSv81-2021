package com.ftn.ts.repository;

import com.ftn.ts.model.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<BaseUser, Long> {
    Optional<BaseUser> findByEmail(String email);
}

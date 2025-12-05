package com.ftn.ts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ftn.ts.model.UserOD;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserODRepository extends JpaRepository<UserOD, Long> {
    Optional<UserOD> findByEmail(String username);
}

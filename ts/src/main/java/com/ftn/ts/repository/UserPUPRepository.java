package com.ftn.ts.repository;

import com.ftn.ts.model.UserPUP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserPUPRepository extends JpaRepository<UserPUP, Long> {
    Optional<UserPUP> findByEmail(String email);
}

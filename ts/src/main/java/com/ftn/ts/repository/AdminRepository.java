package com.ftn.ts.repository;

import com.ftn.ts.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin getAdminByEmail(String email);
}

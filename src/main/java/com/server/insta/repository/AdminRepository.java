package com.server.insta.repository;

import com.server.insta.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByAdminId(String adminId);
}

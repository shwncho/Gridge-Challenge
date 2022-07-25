package com.server.insta.repository;

import com.server.insta.config.Entity.Status;
import com.server.insta.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndStatus(String email, Status status);
    Optional<User> findByIdAndStatus(Long id, Status status);
    User findByEmail(String email);
    boolean existsByEmailAndStatus(String email, Status status);

}

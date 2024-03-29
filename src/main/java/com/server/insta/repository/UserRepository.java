package com.server.insta.repository;

import com.server.insta.config.Entity.Status;
import com.server.insta.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndStatus(String username, Status status);
    Optional<User> findByEmailAndStatus(String email, Status status);
    Optional<User> findByIdAndStatus(Long id, Status status);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}

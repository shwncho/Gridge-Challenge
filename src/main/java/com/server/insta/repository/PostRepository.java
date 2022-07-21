package com.server.insta.repository;

import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    Optional<Post> findByIdAndStatus(Long id, Status status);
}

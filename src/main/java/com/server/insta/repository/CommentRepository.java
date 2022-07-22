package com.server.insta.repository;

import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    Optional<Comment> findByIdAndStatus(Long id, Status status);
}

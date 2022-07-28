package com.server.insta.repository;

import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Post;
import com.server.insta.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    Optional<Post> findByIdAndStatus(Long id, Status status);

    List<Post> findAllByUser(User user);

    int countByUser(User user);

}

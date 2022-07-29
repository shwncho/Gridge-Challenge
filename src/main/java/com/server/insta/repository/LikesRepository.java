package com.server.insta.repository;

import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Comment;
import com.server.insta.domain.Likes;
import com.server.insta.domain.Post;
import com.server.insta.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes,Long> {

    Likes findByUserAndPost(User user, Post post);

    Likes findByUserAndComment(User user, Comment comment);

    List<Likes> findAllByPost(Post post);

    int countByPost(Post post);

    boolean existsByUserAndPostAndStatus(User user, Post post, Status status);

    boolean existsByUserAndPost(User user, Post post);

    boolean existsByUserAndCommentAndStatus(User user, Comment comment, Status status);

    boolean existsByUserAndComment(User user, Comment comment);
}

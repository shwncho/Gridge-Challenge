package com.server.insta.repository;

import com.server.insta.domain.Media;
import com.server.insta.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media,Long> {

    List<Media> findAllByPost(Post post);
}

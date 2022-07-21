package com.server.insta.repository;

import com.server.insta.domain.Post;
import com.server.insta.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {

    List<Tag> findAllByPost(Post post);
}

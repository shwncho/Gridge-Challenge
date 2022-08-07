package com.server.insta.repository;

import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Follow;
import com.server.insta.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow,Long> {

    List<Follow> findAllByFromUserAndToUserStatus(User fromUser, Status status);

    List<Follow> findAllByToUserAndFromUserStatus(User toUser, Status status);

    Follow findByFromUserAndToUser(User fromUser, User toUser);

    int countByFromUser(User fromUser);

    int countByToUser(User toUser);

    boolean existsByFromUserAndToUserAndStatus(User fromUser, User toUser, Status status);

    boolean existsByFromUserAndToUser(User fromUser, User toUser);
}

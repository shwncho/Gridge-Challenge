package com.server.insta.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Follow;
import com.server.insta.domain.Post;
import com.server.insta.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.server.insta.domain.QFollow.follow;
import static com.server.insta.domain.QLikes.likes;

@Repository
@RequiredArgsConstructor
public class QueryRepository {

    private final JPAQueryFactory queryFactory;


    public boolean existFollowByUser(User fromUser, User toUser){
        Integer fetchFirst = queryFactory
                .selectOne()
                .from(follow)
                .where(follow.fromUser.eq(fromUser), follow.toUser.eq(toUser))
                .fetchFirst();

        return fetchFirst != null;
    }

    public boolean existLikeByUserAndPost(User user, Post post){
        Integer fetchFirst = queryFactory
                .selectOne()
                .from(likes)
                .where(likes.user.eq(user),likes.post.eq(post))
                .fetchFirst();

        return fetchFirst != null;
    }



}

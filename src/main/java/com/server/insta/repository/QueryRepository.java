package com.server.insta.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Follow;
import com.server.insta.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.server.insta.domain.QFollow.follow;

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



}

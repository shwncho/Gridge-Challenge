package com.server.insta.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.insta.config.Entity.Authority;
import com.server.insta.config.Entity.Status;
import com.server.insta.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.server.insta.domain.QFollow.follow;
import static com.server.insta.domain.QLikes.likes;
import static com.server.insta.domain.QComment.comment;
import static com.server.insta.domain.QPost.post;
import static com.server.insta.domain.QMedia.media1;
import static com.server.insta.domain.QTag.tag;
import static com.server.insta.domain.QMessage.message;
import static com.server.insta.domain.QUser.user;
import static com.server.insta.domain.QReport.report;

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
                .where(likes.user.eq(user),likes.post.eq(post),likes.status.eq(Status.ACTIVE))
                .fetchFirst();

        return fetchFirst != null;
    }


    public Optional<Comment> findCommentByIdWithParent(Long id){
        return Optional.ofNullable(queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.id.eq(id), comment.status.eq(Status.ACTIVE))
                .fetchOne());
    }

    public List<Message> findMessagesByUser(User user, User otherUser){
        return queryFactory.selectFrom(message)
                .where((message.sender.eq(user).and(message.receiver.eq(otherUser)))
                        .or(message.sender.eq(otherUser).and(message.receiver.eq(user))))
                .orderBy(message.id.desc())
                .fetch();
    }

    //feed 조회 페이징
    public List<Post> findAllPost(User user, Long lastPostId, int size){
        return queryFactory.selectDistinct(post)
                .from(post)
                .join(media1).on(media1.post.eq(post))
                .leftJoin(tag).on(tag.post.eq(post))
                .where(ltPostId(lastPostId),
                        post.user.eq(
                        JPAExpressions
                                .select(follow.toUser)
                                .from(follow)
                                .where(follow.fromUser.eq(user),follow.toUser.status.eq(Status.ACTIVE))
                ).or(post.user.eq(user)), (post.status.eq(Status.ACTIVE)))
                .orderBy(post.id.desc())
                .limit(size)
                .fetch();
    }

    public List<Post> findAllMyPost(User user, Long lastPostId, int size){
        return queryFactory.selectDistinct(post)
                .from(post)
                .where(ltPostId(lastPostId),
                        post.user.eq(user), post.status.eq(Status.ACTIVE))
                .orderBy(post.id.desc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression ltPostId(Long postId){
        if(postId == null)  return null;
        return post.id.lt(postId);
    }

    //게시물의 댓글조회 페이징
    public List<Comment> findCommentsByPost(Post post, Long lastCommentId, int size){
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(ltCommentId(lastCommentId)
                        ,comment.post.eq(post), comment.status.eq(Status.ACTIVE))
                .orderBy(
                        comment.parent.id.desc().nullsFirst(),
                        comment.createdAt.desc()
                )
                .limit(size)
                .fetch();
    }

    private BooleanExpression ltCommentId(Long commentId){
        if(commentId == null)   return null;
        return comment.id.lt(commentId);
    }


    //신고 조회 페이징(관리자)
    public List<Report> findAllReports(int pageIndex, int pageSize){
        return queryFactory.selectFrom(report)
                .orderBy(report.createdAt.desc())
                .offset(pageIndex)
                .limit(pageSize)
                .fetch();
    }


    //회원 검색 다중 조건
    public List<User> findAllByUsers(String name, String username, String joinedDate, Status status, int pageIndex, int pageSize){
        return queryFactory
                .selectFrom(user)
                .where(user.authority.eq(Authority.ROLE_USER),
                        eqName(name),
                        eqUsername(username),
                        eqCreatedAt(joinedDate),
                        eqStatus(status))
                .orderBy(user.createdAt.desc())
                .offset(pageIndex * pageSize)
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression eqName(String name){
        if(!StringUtils.hasText(name))  return null;
        return user.name.eq(name);
    }

    private BooleanExpression eqUsername(String username){
        if(!StringUtils.hasText(username))  return null;
        return user.username.eq(username);
    }

    private BooleanExpression eqCreatedAt(String joinedDate) {
        if (!StringUtils.hasText(joinedDate)) return null;
        else {
            LocalDate date = LocalDate.parse(joinedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return user.createdAt.between(date.atStartOfDay(), LocalDateTime.of(date, LocalTime.MAX));

        }
    }
    private BooleanExpression eqStatus(Status status){
        if(status==null)    return null;
        else if(!StringUtils.hasText(status.toString())) return null;
        return user.status.eq(status);
    }

    //피드 검색 다중 조건
    public List<Post> findAllByPosts(String username, String createdDate, Status status, int pageIndex, int pageSize){
        return queryFactory
                .selectDistinct(post)
                .from(post)
                .join(media1).on(media1.post.eq(post))
                .leftJoin(tag).on(tag.post.eq(post))
                .where( eqUsernameByPost(username),
                        eqCreatedAtByPost(createdDate),
                        eqStatusByPost(status))
                .orderBy(post.createdAt.desc())
                .offset(pageIndex * pageSize)
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression eqUsernameByPost(String username){
        if(!StringUtils.hasText(username))  return null;
        return post.user.username.eq(username);
    }

    private BooleanExpression eqCreatedAtByPost(String createdDate) {
        if (!StringUtils.hasText(createdDate)) return null;
        else {
            LocalDate date = LocalDate.parse(createdDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return post.createdAt.between(date.atStartOfDay(), LocalDateTime.of(date, LocalTime.MAX));

        }
    }
    private BooleanExpression eqStatusByPost(Status status){
        if(status==null)    return null;
        else if(!StringUtils.hasText(status.toString())) return null;
        return post.status.eq(status);
    }


}

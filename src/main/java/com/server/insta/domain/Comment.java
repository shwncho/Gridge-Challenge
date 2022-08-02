package com.server.insta.domain;

import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.config.Entity.Status;
import com.server.insta.dto.response.GetCommentsResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @Column(length = 200)
    private String content;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "comment")
    private List<Likes> likes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> child = new ArrayList<>();

    private int reportCount; // 신고당한횟수

    public void addReportCount(){
        this.reportCount++;
    }

    public void hidePost(){
        this.reportCount=0;
        this.status=Status.BLOCK;
    }

    public void deleteComment(){
        this.status = Status.DELETED;
    }
    public void restoreComment() { this.status = Status.ACTIVE; }


    @Builder
    public Comment(User user, Post post, String content, Comment parent){
        this.user = user;
        this.post = post;
        this.content = content;
        this.parent = parent;
        this.status = Status.ACTIVE;
    }

    public GetCommentsResponseDto toCommentsDto(int likeCount){
        return GetCommentsResponseDto.builder()
                .commentId(id)
                .content(content)
                .userId(user.getId())
                .username(user.getUsername())
                .profileImgUrl(user.getProfileImgUrl())
                .likeCount(likeCount)
                .build();
    }


}

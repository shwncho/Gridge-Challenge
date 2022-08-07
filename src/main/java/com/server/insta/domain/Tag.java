package com.server.insta.domain;

import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.config.Entity.Status;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @NotNull
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Tag(String content, Post post){
        this.content = content;
        this.post = post;
        post.getTags().add(this);
        this.status = Status.ACTIVE;
    }

    public Tag(String content){
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // == 연관관계 메서드 == //
    public void setPost(Post post) {
        this.post = post;
        post.getTags().add(this);
    }

    public void deleteTag(){
        this.status = Status.DELETED;
    }
}

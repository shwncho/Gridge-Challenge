package com.server.insta.domain;

import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.config.Entity.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Long id;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String media;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private Status status;


    @Builder
    public Media(String media, Post post) {
        this.post = post;
        this.media = media;
        post.getMedias().add(this);
        this.status = Status.ACTIVE;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public void setPost(Post post) {
        this.post = post;
        post.getMedias().add(this);

    }

    public void deleteMedia(){
        this.status = Status.DELETED;
    }

}

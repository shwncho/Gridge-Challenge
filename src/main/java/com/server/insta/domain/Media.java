package com.server.insta.domain;

import com.server.insta.config.Entity.BaseTimeEntity;
import lombok.AccessLevel;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String media;

    public Media(String media) {
        this.media = media;
    }

    public Media(String media, Post post) {
        this.post = post;
        this.media = media;
    }

    public void setPost(Post post) {
        this.post = post;
        post.getMedias().add(this);

    }

}

package com.server.insta.domain;

import com.server.insta.config.Entity.BaseTimeEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String image;

    public Media(String image) {
        this.image = image;
    }

    @Builder
    public Media(Post post, String image) {
        this.post = post;
        this.image = image;
    }

    public void setPost(Post post) {
        this.post = post;
        post.getMedias().add(this);

    }

}

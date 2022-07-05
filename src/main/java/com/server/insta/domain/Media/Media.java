package com.server.insta.domain.Media;

import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.domain.Post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "media_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "post_id")
    private Post post;

    @Column(columnDefinition = "TEXT")
    private String media;


}

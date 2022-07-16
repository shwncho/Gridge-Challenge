package com.server.insta.domain.Post;

import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.domain.Likes.Likes;
import com.server.insta.domain.Media.Media;
import com.server.insta.domain.Tag.Tag;
import com.server.insta.domain.User.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotNull
    private String caption; // 게시물 설명

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Media> medias= new ArrayList<>();


}

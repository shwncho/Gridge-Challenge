package com.server.insta.domain.Post;

import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.domain.Likes.Likes;
import com.server.insta.domain.Media.Media;
import com.server.insta.domain.Tag.Tag;
import com.server.insta.domain.User.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String caption; // 게시물 설명

    //Post 생성과 동시에 태그를 추가하기 때문에 CascadeType.ALL 설정
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}

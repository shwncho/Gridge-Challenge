package com.server.insta.domain;

import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.config.Entity.Status;
import com.server.insta.dto.response.PostResponseDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String caption; // 게시물 설명

    @OneToMany(mappedBy = "post",orphanRemoval = true)
    private List<Media> medias = new ArrayList<>();

    @OneToMany(mappedBy = "post",orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post",orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Post(String caption, User user, Status status){
        this.caption = caption;
        this.user = user;
        this.status = status;
    }

    public void deletePost(){
        this.status = Status.DELETED;
    }




}

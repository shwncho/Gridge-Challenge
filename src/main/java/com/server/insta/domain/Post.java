package com.server.insta.domain;

import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.config.Entity.Status;
import lombok.*;

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

    @Column(length = 1000)
    private String caption; // 게시물 설명

    @OneToMany(mappedBy = "post")
    private List<Media> medias = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Likes> likes = new ArrayList<>();

    private int reportCount; // 신고당한횟수


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void addReportCount(){
        this.reportCount++;
    }

    public void hidePost(){
        this.reportCount=0;
        this.status=Status.BLOCK;
    }

    @Builder
    public Post(String caption, User user, Status status){
        this.caption = caption;
        this.user = user;
        this.status = status;
    }

    public void deletePost(){
        this.status = Status.DELETED;
    }

    public void restorePost(){this.status = Status.ACTIVE;}

    public void setCaption(String caption) {
        this.caption = caption;
    }


}

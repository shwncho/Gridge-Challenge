package com.server.insta.domain;

import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.config.Entity.FollowStatus;
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
public class Follow extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id" )
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private FollowStatus followStatus;


    @Builder
    public Follow(User fromUser, User toUser, Status status, FollowStatus followStatus){
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.status = status;
        this.followStatus = followStatus;
    }

    public void deleteFollow(){
        this.status = Status.DELETED;
    }

    public void changeStatusByClose(){
        this.status = Status.INACTIVE;
        this.followStatus = FollowStatus.WAIT;
    }

    public void changeStatusByOpen(){
        this.status = Status.ACTIVE;
    }

    public void approveFollow(){
        this.status = Status.ACTIVE;
        this.followStatus = FollowStatus.COMPLETE;
    }


}

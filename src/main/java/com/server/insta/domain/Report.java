package com.server.insta.domain;

import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.dto.response.GetReportsResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    private String reason; //신고 사유

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public Report(String reason, Post post, Comment comment) {
        this.reason = reason;
        this.post = post;
        this.comment = comment;
    }

    public GetReportsResponseDto toReport(LocalDateTime createdAt){
        return GetReportsResponseDto.builder()
                .postId(post.getId())
                .commentId(comment!=null ? comment.getId() : null)
                .reason(reason)
                .createdAt(createdAt)
                .build();
    }


}

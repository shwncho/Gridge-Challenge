package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.Comment;
import com.server.insta.domain.Post;
import com.server.insta.domain.User;
import com.server.insta.dto.request.CreateCommentRequestDto;
import com.server.insta.dto.response.GetCommentsResponseDto;
import com.server.insta.dto.response.GetPostResponseDto;
import com.server.insta.dto.response.PostMapToCommentsDto;
import com.server.insta.repository.CommentRepository;
import com.server.insta.repository.PostRepository;
import com.server.insta.repository.QueryRepository;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final QueryRepository queryRepository;

    @Transactional
    public void createComment(String email, Long id, CreateCommentRequestDto dto){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));
        Post post = postRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(()-> new BusinessException(POST_NOT_EXIST));

        // 대댓글에서 parent는 무조건 null만 가능

        commentRepository.save(Comment.builder()
                .user(user)
                .post(post)
                .content(dto.getContent())
                .parent(dto.getParentId() != null ? commentRepository.findByIdAndStatus(dto.getParentId(), Status.ACTIVE)
                        .orElseThrow(()-> new BusinessException(COMMENT_NOT_EXIST)) : null)
                .build());


    }


    @Transactional(readOnly = true)
    public PostMapToCommentsDto getComments(Long postId, Long lastCommentId, int pageSize){
        Post post = postRepository.findByIdAndStatus(postId, Status.ACTIVE)
                .orElseThrow(()-> new BusinessException(POST_NOT_EXIST));

        List<Comment> comments = queryRepository.findCommentsByPost(post,lastCommentId,pageSize);

        List<GetCommentsResponseDto> commentList = new ArrayList<>();
        Map<Long, GetCommentsResponseDto> map = new HashMap<>();
        comments.forEach(c -> {
            GetCommentsResponseDto dto = c.toCommentsDto();
            dto.setCreatedComment(calculateCreatedTime(c.getCreatedAt()));
            map.put(dto.getCommentId(), dto);
            if(c.getParent() != null)   map.get(c.getParent().getId()).getChildren().add(dto);
            else    commentList.add(dto);
        });

        return PostMapToCommentsDto.builder()
                .userId(post.getUser().getId())
                .nickName(post.getUser().getNickName())
                .profileImgUrl(post.getUser().getProfileImgUrl())
                .caption(post.getCaption())
                .createdPost(calculateCreatedTime(post.getCreatedAt()))
                .getCommentsResponseDtoList(commentList)
                .build();

    }

    @Transactional
    public void deletePost(String email, Long id){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        Comment comment = queryRepository.findCommentByIdWithParent(id)
                .orElseThrow(()-> new BusinessException(COMMENT_NOT_EXIST));

        if(user.getId() != comment.getUser().getId()){
            throw new BusinessException(USER_NOT_INVALID);
        }

        if(comment.getChild().size() != 0)  comment.deleteComment();
        else    commentRepository.delete(getDeletableAncestorComment(comment));


    }

    private Comment getDeletableAncestorComment(Comment comment){
        Comment parent = comment.getParent();
        if(parent != null && parent.getChild().size() ==1 && parent.getStatus() == Status.DELETED)  return  getDeletableAncestorComment(parent);
        return comment;
    }

    //작성시간 계산
    private String calculateCreatedTime(LocalDateTime createdAt){
        LocalDateTime now = LocalDateTime.now();

        //1개월 이후
        if(ChronoUnit.MONTHS.between(createdAt, now)>0) return createdAt.getMonthValue()+"월" + createdAt.getDayOfMonth()+"일";
            //24시간 이후 ~ 1개월 이내
        else if(ChronoUnit.DAYS.between(createdAt, now)>0)  return ChronoUnit.DAYS.between(createdAt, now) +"일 전";
            //1시간 이후 ~ 24시간 이내
        else if(ChronoUnit.HOURS.between(createdAt, now)>0) return ChronoUnit.HOURS.between(createdAt, now) + "시간 전";
            //1시간 이내
        else if(ChronoUnit.MINUTES.between(createdAt, now)>0)   return ChronoUnit.MINUTES.between(createdAt, now) + "분 전";
            //1분 이내
        else{
            return ChronoUnit.SECONDS.between(createdAt, now) + "초 전";
        }
    }
}

package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.Comment;
import com.server.insta.domain.Post;
import com.server.insta.domain.User;
import com.server.insta.dto.request.CreateCommentRequestDto;
import com.server.insta.dto.response.GetCommentsResponseDto;
import com.server.insta.repository.CommentRepository;
import com.server.insta.repository.PostRepository;
import com.server.insta.repository.QueryRepository;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<GetCommentsResponseDto> getComments(Long id){
        Post post = postRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(()-> new BusinessException(POST_NOT_EXIST));

        List<Comment> comments = queryRepository.findCommentsByPost(post);

        List<GetCommentsResponseDto> result = new ArrayList<>();
        Map<Long, GetCommentsResponseDto> map = new HashMap<>();
        comments.forEach(c -> {
            GetCommentsResponseDto dto = c.toCommentsDto();
            map.put(dto.getCommentId(), dto);
            if(c.getParent() != null)   map.get(c.getParent().getId()).getChildren().add(dto);
            else    result.add(dto);
        });

        return result;

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
}

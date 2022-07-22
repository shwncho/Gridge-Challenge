package com.server.insta.service;

import com.server.insta.config.Entity.Status;
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

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final QueryRepository queryRepository;

    @Transactional
    public void createPost(String email, Long id, CreateCommentRequestDto dto){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(()->new RuntimeException("존재하지 않는 유저 입니다."));
        Post post = postRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(()-> new RuntimeException("존재하지 않는 게시물 입니다."));

        commentRepository.save(Comment.builder()
                .user(user)
                .post(post)
                .content(dto.getContent())
                .parent(dto.getParentId() != null ? commentRepository.findByIdAndStatus(dto.getParentId(), Status.ACTIVE)
                        .orElseThrow(()-> new RuntimeException("지워진 댓글에는 대댓글을 달 수 없습니다.")) : null)
                .build());


    }

    @Transactional(readOnly = true)
    public List<GetCommentsResponseDto> getComments(Long id){
        Post post = postRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(()-> new RuntimeException("존재하지 않는 게시물 입니다."));

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
}

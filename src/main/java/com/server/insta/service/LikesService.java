package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.Comment;
import com.server.insta.domain.Likes;
import com.server.insta.domain.Post;
import com.server.insta.domain.User;
import com.server.insta.dto.response.GetLikeUsersResponseDto;
import com.server.insta.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void actLike(String email, Long id){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
        Post post = postRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(POST_NOT_EXIST));


        //좋아요가 이미 되어 있으면 -> 취소
        if(likesRepository.existsByUserAndPostAndStatus(user,post,Status.ACTIVE)){
            likesRepository.findByUserAndPost(user,post).deleteLikes();
        }
        //좋아요가 되어있지 않으면 -> 좋아요
        else{
            //DB에 존재하면
            if(likesRepository.existsByUserAndPost(user,post)){
                likesRepository.findByUserAndPost(user,post).changeStatus();
            }

            //DB에 존재하지 않으면
            else {
                Likes postLike = Likes.builder()
                        .user(user)
                        .post(post)
                        .build();
                likesRepository.save(postLike);
                post.getLikes().add(postLike);

            }
        }

    }

    @Transactional
    public void actLikeToComment(String email, Long id){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
        Comment comment = commentRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(POST_NOT_EXIST));


        //좋아요가 이미 되어 있으면 -> 취소
        if(likesRepository.existsByUserAndCommentAndStatus(user,comment,Status.ACTIVE)){
            likesRepository.findByUserAndComment(user,comment).deleteLikes();
        }
        //좋아요가 되어있지 않으면 -> 좋아요
        else{
            //DB에 존재하면
            if(likesRepository.existsByUserAndComment(user,comment)){
                likesRepository.findByUserAndComment(user,comment).changeStatus();
            }

            //DB에 존재하지 않으면
            else {
                Likes commentLike = Likes.builder()
                        .user(user)
                        .comment(comment)
                        .build();
                likesRepository.save(commentLike);
                comment.getLikes().add(commentLike);
            }
        }

    }


    @Transactional(readOnly = true)
    public List<GetLikeUsersResponseDto> getLikeUsers(Long id){
        Post post = postRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(POST_NOT_EXIST));

        List<User> users = likesRepository.findAllByPost(post).stream()
                .map(Likes::getUser).collect(Collectors.toList());

        return users.stream()
                .map(User::toLikeUsers)
                .collect(Collectors.toList());



    }
}

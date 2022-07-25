package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.Likes;
import com.server.insta.domain.Post;
import com.server.insta.domain.User;
import com.server.insta.dto.response.GetLikeUsersResponseDto;
import com.server.insta.repository.LikesRepository;
import com.server.insta.repository.PostRepository;
import com.server.insta.repository.QueryRepository;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final QueryRepository queryRepository;

    public void saveLike(String email, Long id){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
        Post post = postRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(POST_NOT_EXIST));

        if(queryRepository.existLikeByUserAndPost(user,post)){
            throw new BusinessException(LIKE_EXIST_POST);
        }


        likesRepository.save(Likes.builder()
                .user(user)
                .post(post)
                .build());
    }

    public void deleteLike(String email, Long id){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
        Post post = postRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(POST_NOT_EXIST));

        if(!queryRepository.existLikeByUserAndPost(user,post)){
            throw new BusinessException(LIKE_NOT_EXIST_POST);
        }

        likesRepository.delete(likesRepository.findByUserAndPost(user,post));
    }

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

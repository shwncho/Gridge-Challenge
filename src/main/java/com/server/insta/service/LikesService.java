package com.server.insta.service;

import com.server.insta.config.Entity.Status;
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

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final QueryRepository queryRepository;

    public void saveLike(String email, Long id){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저 입니다."));
        Post post = postRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시물 입니다."));

        if(queryRepository.existLikeByUserAndPost(user,post)){
            throw new RuntimeException("이미 좋아요한 게시물 입니다.");
        }


        likesRepository.save(Likes.builder()
                .user(user)
                .post(post)
                .build());
    }

    public void deleteLike(String email, Long id){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저 입니다."));
        Post post = postRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시물 입니다."));

        if(!queryRepository.existLikeByUserAndPost(user,post)){
            throw new RuntimeException("좋아요를 하지 않은 게시물 입니다.");
        }

        likesRepository.delete(likesRepository.findByUserAndPost(user,post));
    }

    public List<GetLikeUsersResponseDto> getLikeUsers(Long id){
        Post post = postRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시물 입니다."));

        List<User> users = likesRepository.findAllByPost(post).stream()
                .map(Likes::getUser).collect(Collectors.toList());

        return users.stream()
                .map(User::toLikeUsers)
                .collect(Collectors.toList());



    }
}

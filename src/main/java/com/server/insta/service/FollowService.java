package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Follow;
import com.server.insta.repository.FollowRepository;
import com.server.insta.domain.User;
import com.server.insta.repository.QueryRepository;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final QueryRepository queryRepository;

    @Transactional
    public void follow(String email, Long toUserid){
        User fromUser = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저 입니다."));
        User toUser = userRepository.findByIdAndStatus(toUserid, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저 입니다."));

        if(queryRepository.existFollowByUserId(fromUser, toUser)){
            throw new RuntimeException("이미 팔로우한 유저 입니다.");
        }

        Follow follow = Follow.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .build();

        followRepository.save(follow);


    }
}

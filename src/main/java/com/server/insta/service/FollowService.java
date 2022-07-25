package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.Follow;
import com.server.insta.dto.response.GetFollowerResponseDto;
import com.server.insta.dto.response.GetFollowingResponseDto;
import com.server.insta.repository.FollowRepository;
import com.server.insta.domain.User;
import com.server.insta.repository.QueryRepository;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;

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
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
        User toUser = userRepository.findByIdAndStatus(toUserid, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));

        if(queryRepository.existFollowByUser(fromUser, toUser)){
            throw new BusinessException(FOLLOW_EXIST_RELATIONSHIP);
        }

        Follow follow = Follow.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .build();

        followRepository.save(follow);


    }

    @Transactional
    public void unFollow(String email, Long toUserid){
        User fromUser = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
        User toUser = userRepository.findByIdAndStatus(toUserid, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));

        if(!queryRepository.existFollowByUser(fromUser, toUser)){
            throw new BusinessException(FOLLOW_NOT_EXIST_RELATIONSHIP);
        }

        //언팔했다가 팔로우 할경우 save를 통해 DB에 넣어주기 위해 status를 사용안함.
        //만약 status를 사용하면 언팔한 관계가 다시 팔로우할 때 조회를 한 이후 변환해야하므로 로직이 더 지저분해짐
        followRepository.delete(followRepository.findByFromUserAndToUser(fromUser,toUser));

    }

    @Transactional(readOnly = true)
    public List<GetFollowingResponseDto> getFollowing(Long id){
        User fromUser = userRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));

        return followRepository.findAllByFromUser(fromUser)
                .stream().map(Follow::getToUser)
                .collect(Collectors.toList())
                .stream().map(User::toFollowing)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GetFollowerResponseDto> getFollower(Long id){
        User toUser = userRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));

        return followRepository.findAllByToUser(toUser)
                .stream().map(Follow::getFromUser)
                .collect(Collectors.toList())
                .stream().map(User::toFollower)
                .collect(Collectors.toList());
    }
}

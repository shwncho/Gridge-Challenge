package com.server.insta.service;

import com.server.insta.config.Entity.FollowStatus;
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


    @Transactional
    public void actFollow(String email, Long id){
        User fromUser = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
        User toUser = userRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));

        if(fromUser.getId() == toUser.getId()){
            throw new BusinessException(FOLLOW_NOT_SELF);
        }

        //팔로우가 이미 되어있으면 -> 팔로우 취소
        if(followRepository.existsByFromUserAndToUserAndStatus(fromUser, toUser, Status.ACTIVE)){
            followRepository.findByFromUserAndToUser(fromUser, toUser).deleteFollow();
        }
        //팔로우 요청을 기다리는 상태에서 누르면 -> 요청 취소
        else if(followRepository.existsByFromUserAndToUserAndStatus(fromUser, toUser, Status.INACTIVE)){
            followRepository.delete(followRepository.findByFromUserAndToUser(fromUser, toUser));
        }
        //팔로우가 되어 있지 않으면 -> 팔로우
        else{
            //DB에 존재하면(=이전에 팔로우 관계였다면)
            if(followRepository.existsByFromUserAndToUser(fromUser, toUser)){
                //팔로우 요청하려는 대상이 비공개 계정일 경우
                if(!toUser.isPublic){
                    followRepository.findByFromUserAndToUser(fromUser,toUser).changeStatusByClose();
                }
                //팔로우 요청하려는 대상이 공개 계정일 경우
                else{
                    followRepository.findByFromUserAndToUser(fromUser, toUser).changeStatusByOpen();
                }
            }
            //DB에 존재하지 않는다면
            else{
                //팔로우 요청하려는 대상이 비공개 계정일 경우
                if(!toUser.isPublic){
                    followRepository.save(Follow.builder()
                            .fromUser(fromUser)
                            .toUser(toUser)
                            .status(Status.INACTIVE)
                            .followStatus(FollowStatus.WAIT)
                            .build());
                }
                //팔로우 요청하려는 대상이 공개 계정일 경우
                else{
                    followRepository.save(Follow.builder()
                            .fromUser(fromUser)
                            .toUser(toUser)
                            .status(Status.ACTIVE)
                            .followStatus(FollowStatus.COMPLETE)
                            .build());
                }
            }
        }


    }

    @Transactional
    public void approveFollow(String email, Long id){
        User toUser = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
        User fromUser = userRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));

        if(fromUser.getId() == toUser.getId()){
            throw new BusinessException(FOLLOW_NOT_SELF);
        }

        followRepository.findByFromUserAndToUser(fromUser, toUser).approveFollow();

    }

    @Transactional
    public void denyFollow(String email, Long id){
        User toUser = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
        User fromUser = userRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));

        if(fromUser.getId() == toUser.getId()){
            throw new BusinessException(FOLLOW_NOT_SELF);
        }

        followRepository.delete(followRepository.findByFromUserAndToUser(fromUser, toUser));
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

package com.server.insta.service;

import com.server.insta.config.Entity.Provider;
import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.Media;
import com.server.insta.domain.Post;
import com.server.insta.domain.Tag;
import com.server.insta.domain.User;
import com.server.insta.dto.request.ResetPasswordRequestDto;
import com.server.insta.dto.request.UpdateProfileRequestDto;
import com.server.insta.dto.response.GetFeedResponseDto;
import com.server.insta.dto.response.GetPostResponseDto;
import com.server.insta.dto.response.GetUserPageDto;
import com.server.insta.log.NoLogging;
import com.server.insta.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final QueryRepository queryRepository;
    private final PasswordEncoder passwordEncoder;

    @NoLogging
    @Transactional(readOnly = true)
    public GetUserPageDto getUserPage(Long userId, Long lastPostId, int pageSize){
        User user = userRepository.findByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(()-> new BusinessException(USER_NOT_EXIST));

        List<Post> pagePosts = queryRepository.findAllMyPost(user,lastPostId,pageSize);

        List<GetFeedResponseDto> result = new ArrayList<>();

        int postCount = postRepository.countByUser(user);
        int followerCount = followRepository.countByToUser(user);
        int followingCount = followRepository.countByFromUser(user);

        pagePosts.forEach(post ->{
            Long postId = post.getId();
            int likeCount = likesRepository.countByPost(post);
            int commentCount = commentRepository.countByPost(post);

            GetPostResponseDto dto = GetPostResponseDto.builder()
                    .userId(post.getUser().getId())
                    .username(post.getUser().getUsername())
                    .profileImgUrl(post.getUser().getProfileImgUrl())
                    .caption(post.getCaption())
                    .likeCount(likeCount)
                    .commentCount(commentCount)
                    .medias(
                            post.getMedias().stream()
                                    .map(Media::getMedia)
                                    .collect(Collectors.toList())
                    )
                    .tags(
                            post.getTags().stream()
                                    .map(Tag::getContent)
                                    .collect(Collectors.toList())
                    )
                    .createdPost(calculateCreatedTime(post.getCreatedAt()))
                    .build();

            result.add(GetFeedResponseDto.builder()
                    .postId(postId)
                    .getPostResponseDto(dto)
                    .build());

        });

        return GetUserPageDto.builder()
                .username(user.getUsername())
                .profileImgUrl(user.getProfileImgUrl())
                .name(user.getName())
                .introduce(user.getIntroduce())
                .website(user.getWebsite())
                .postCount(postCount)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .getFeedResponseDtos(result)
                .build();

    }

    @Transactional
    public void resetPassword(String username, ResetPasswordRequestDto dto){
        User user = userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(()-> new BusinessException(USER_NOT_EXIST));

        if(user.getProvider()!= Provider.NORMAL){
            throw new BusinessException(USER_NOT_CHANGE_PASSWORD);
        }

        user.changePassword(passwordEncoder.encode(dto.getPassword()));
    }

    @Transactional
    public void updateProfile(String username, UpdateProfileRequestDto dto){
        User user = userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        //본인의 사용자 이름이 아니고, 다른 유저가 사용자 이름을 사용하고 있을 때
        if(!user.getUsername().equals(dto.getUsername()) && userRepository.existsByUsername(dto.getUsername())){
            throw new BusinessException(USER_EXIST_USERNAME);
        }

        // 이름을 변경하려 할 때
        if(!dto.getName().equals(user.getName())){
            //이름을 변경한적이 없거나 or 이전 변경기간이 14일이 넘었을 때
            if(user.getUpdateNameDate()==null || ChronoUnit.DAYS.between(user.getUpdateNameDate(), LocalDateTime.now())>14){
                user.changeUpdateNameDate();
            }
            //변경기간이 14일 이내 일 때
            else{
                if(user.getNameCount()==2)   throw new BusinessException(USER_NOT_CHANGE_NAME);
                else    user.addNameCount();
            }
        }

        // 사용자 이름을 변경하려 할 때
        if(!dto.getUsername().equals(user.getUsername())){
            // 사용자 이름을 변경한적이 없거나 or 이전 변경기간이 14일이 넘었을 때
            if(user.getUpdateUsernameDate()==null || ChronoUnit.DAYS.between(user.getUpdateUsernameDate(), LocalDateTime.now())>14){
                user.changeUpdateUsernameDate();
            }
            //변경기간이 14일 이내 일 때
            else{
                if(user.getUsernameCount()==2)   throw new BusinessException(USER_NOT_CHANGE_USERNAME);
                else    user.addUsernameCount();
            }
        }



        user.changeProfile(dto.getProfileImgUrl(), dto.getName(), dto.getUsername(), dto.getWebsite(), dto.getIntroduce());

    }

    @Transactional
    public void updateStatus(String username){
        User user = userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(user.isPublic)    user.closePublic();
        else    user.openPublic();


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

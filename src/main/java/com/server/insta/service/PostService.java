package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.Media;
import com.server.insta.domain.Post;
import com.server.insta.domain.Tag;
import com.server.insta.dto.request.UpdatePostRequestDto;
import com.server.insta.dto.response.GetFeedResponseDto;
import com.server.insta.dto.response.GetPostResponseDto;
import com.server.insta.repository.*;
import com.server.insta.domain.User;
import com.server.insta.dto.request.CreatePostRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final MediaRepository mediaRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final QueryRepository queryRepository;


    @Transactional
    public void createPost(String email, CreatePostRequestDto dto){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));


        Post post = postRepository.save(dto.toEntity(user));

        mediaRepository.saveAll(dto.getMedias().stream()
                .map(m -> new Media(m,post))
                .collect(Collectors.toList()));

        tagRepository.saveAll(dto.getTags().stream()
                .map(t -> new Tag(t,post))
                .collect(Collectors.toList()));
    }

    @Transactional
    public GetPostResponseDto getPost(Long id){
        Post post = postRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(POST_NOT_EXIST));

        int likeCount = likesRepository.countByPost(post);
        int commentCount = commentRepository.countByPost(post);

        return GetPostResponseDto.builder()
                .userId(post.getUser().getId())
                .nickName(post.getUser().getNickName())
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
    }

    @Transactional(readOnly = true)
    public List<GetFeedResponseDto> getFeed(String email, Long lastPostId, int pageSize){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
        List<Post> pagePost = queryRepository.findAllPost(user, lastPostId, pageSize);

        List<GetFeedResponseDto> result = new ArrayList<>();

        pagePost.forEach(post ->{
            Long postId = post.getId();
            int likeCount = likesRepository.countByPost(post);
            int commentCount = commentRepository.countByPost(post);

            GetPostResponseDto dto = GetPostResponseDto.builder()
                    .userId(post.getUser().getId())
                    .nickName(post.getUser().getNickName())
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

        return result;


    }

    @Transactional
    public void deletePost(String email, Long id){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
        Post post = postRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(POST_NOT_EXIST));

        if(user.getId() != post.getUser().getId()){
            throw new BusinessException(USER_NOT_INVALID);
        }

        post.deletePost();
    }

    @Transactional
    public void updatePost(String email, Long id, UpdatePostRequestDto dto){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
        Post post = postRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(POST_NOT_EXIST));


        if(user.getId() != post.getUser().getId()){
            throw new BusinessException(USER_NOT_INVALID);
        }


        post.setCaption(dto.getCaption());
        post.getMedias().clear();
        post.getTags().clear();

        /**
         * 게시글을 수정할 때 기존 이미지와 태그들을 새로운 값들과 매칭시키는 것보다 지우고 생성하는게 더 효율적이라 판단
         * 이유: 몇 개의 값들이 지워지고 추가될지를 판단할 수 없으므로
         */

        //caption은 변경감지를 이용하여 바꾸고
        //tags,medias는 병합방식을 이용하여 이미지와 태그를 새로 갈아끼운다.
        mediaRepository.deleteAll(mediaRepository.findAllByPost(post));
        tagRepository.deleteAll(tagRepository.findAllByPost(post));

        List<Media> medias=dto.getMedias().stream().map(m -> new Media(m,post)).collect(Collectors.toList());
        post.getMedias().addAll(medias);
        mediaRepository.saveAll(medias);


        List<Tag> tags = dto.getTags().stream().map(t -> new Tag(t,post)).collect(Collectors.toList());
        post.getTags().addAll(tags);
        tagRepository.saveAll(tags);

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

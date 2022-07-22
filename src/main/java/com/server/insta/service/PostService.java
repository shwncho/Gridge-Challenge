package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Media;
import com.server.insta.domain.Post;
import com.server.insta.domain.Tag;
import com.server.insta.dto.request.UpdatePostRequestDto;
import com.server.insta.dto.response.GetPostResponseDto;
import com.server.insta.repository.MediaRepository;
import com.server.insta.repository.PostRepository;
import com.server.insta.repository.TagRepository;
import com.server.insta.domain.User;
import com.server.insta.repository.UserRepository;
import com.server.insta.dto.request.SavePostRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final MediaRepository mediaRepository;

    @Transactional
    public void savePost(String email, SavePostRequestDto dto){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저 입니다."));


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
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시물 입니다."));

        return GetPostResponseDto.builder()
                .caption(post.getCaption())
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
                .build();
    }

    @Transactional
    public void deletePost(String email, Long id){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저 입니다."));
        Post post = postRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시물 입니다."));

        if(user.getId() != post.getUser().getId()){
            throw new RuntimeException("게시물을 삭제할 권한이 없는 유저 입니다.");
        }

        post.deletePost();
    }

    @Transactional
    public void updatePost(String email, Long id, UpdatePostRequestDto dto){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저 입니다."));
        Post post = postRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시물 입니다."));


        if(user.getId() != post.getUser().getId()){
            throw new RuntimeException("게시물을 삭제할 권한이 없는 유저 입니다.");
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


}

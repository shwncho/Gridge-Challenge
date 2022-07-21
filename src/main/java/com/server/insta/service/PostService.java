package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Media;
import com.server.insta.domain.Post;
import com.server.insta.domain.Tag;
import com.server.insta.dto.response.PostResponseDto;
import com.server.insta.repository.MediaRepository;
import com.server.insta.repository.PostRepository;
import com.server.insta.repository.TagRepository;
import com.server.insta.domain.User;
import com.server.insta.repository.UserRepository;
import com.server.insta.dto.request.PostRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public void savePost(String email, PostRequestDto dto){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저 입니다."));


        Post post = dto.toEntity(user);
        postRepository.save(post);


        List<Media> medias = new ArrayList<>();
        for (String media : dto.getMedias()) {
            Media m = new Media(post,media);
            medias.add(m);
        }


        mediaRepository.saveAll(medias);

        List<Tag> tags = new ArrayList<>();
        for (String tag : dto.getTags()) {
            Tag t = new Tag(tag,post);
            tags.add(t);
        }

        tagRepository.saveAll(tags);
    }

    @Transactional
    public PostResponseDto getPost(Long id){
        Post post = postRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시물 입니다."));

        return PostResponseDto.builder()
                .caption(post.getCaption())
                .medias(
                        post.getMedias().stream()
                                .map(Media::getImage)
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


}

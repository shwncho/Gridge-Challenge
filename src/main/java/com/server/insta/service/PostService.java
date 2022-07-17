package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Media.Media;
import com.server.insta.domain.Media.MediaRepository;
import com.server.insta.domain.Post.Post;
import com.server.insta.domain.Post.PostRepository;
import com.server.insta.domain.Tag.Tag;
import com.server.insta.domain.Tag.TagRepository;
import com.server.insta.domain.User.User;
import com.server.insta.domain.User.UserRepository;
import com.server.insta.dto.request.PostRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final MediaRepository mediaRepository;

    @Transactional
    public void postUpload(String email, PostRequestDto dto){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저 입니다."));

        Post post = postRepository.save(dto.toEntity(user));

        List<Tag> tags = new ArrayList<>();
        for(String t : dto.getTags()){
            Tag tag = Tag.builder()
                    .content(t)
                    .post(post)
                    .build();
            //post.getTags().add(tag);
            tags.add(tag);
        }
        tagRepository.saveAll(tags);

        List<Media> medias = new ArrayList<>();
        for(String m : dto.getMedias()){
            medias.add(Media.builder()
                    .post(post)
                    .media(m)
                    .build());
        }
        mediaRepository.saveAll(medias);


    }
}

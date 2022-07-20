package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Media;
import com.server.insta.repository.MediaRepository;
import com.server.insta.domain.Post;
import com.server.insta.repository.PostRepository;
import com.server.insta.domain.Tag;
import com.server.insta.repository.TagRepository;
import com.server.insta.domain.User;
import com.server.insta.repository.UserRepository;
import com.server.insta.dto.request.PostRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Post post = dto.toEntity(user);
        postRepository.save(post);


        for(String m : dto.getMedias()){
            Media media = new Media(m);
            media.setPost(post);
            mediaRepository.save(media);
        }

        for(String t : dto.getTags()){
            Tag tag = new Tag(t);
            tag.setPost(post);
            tagRepository.save(tag);
        }



    }
}

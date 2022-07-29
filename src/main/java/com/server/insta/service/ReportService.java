package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.Post;
import com.server.insta.domain.Report;
import com.server.insta.domain.User;
import com.server.insta.dto.request.ReportPostRequestDto;
import com.server.insta.repository.PostRepository;
import com.server.insta.repository.ReportRepository;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public int reportPost(String email, Long id, ReportPostRequestDto dto){
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        Post post = postRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(()->new BusinessException(POST_NOT_EXIST));

        if(user.getId() == post.getUser().getId()){
            throw new BusinessException(REPORT_NOT_SELF);
        }

        post.addReportCount();

        reportRepository.save(Report.builder()
                .post(post)
                .reason(dto.getReason())
                .build());

        if(post.getReportCount()==10){
            //게시물을 비활성화로 바꿈과 동시에 신고 카운트를 초기화시키고, 관리자가 게시글 삭제 여부를 판단
            post.hidePost();
            return 10;
        }

        return post.getReportCount();
    }
}

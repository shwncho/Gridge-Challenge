package com.server.insta.service;

import com.server.insta.config.Entity.Authority;
import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.Comment;
import com.server.insta.domain.Post;
import com.server.insta.domain.Report;
import com.server.insta.domain.User;
import com.server.insta.dto.response.GetReportsResponseDto;
import com.server.insta.dto.response.GetSearchPostsResponseDto;
import com.server.insta.dto.response.GetSearchUsersResponseDto;
import com.server.insta.dto.response.GetUserInfoResponseDto;
import com.server.insta.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final QueryRepository queryRepository;

    public static final String regexp = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$";

    @Transactional
    public List<GetReportsResponseDto> getReports(String username, int pageIndex, int pageSize){
        User admin = userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }

        List<Report> reports = queryRepository.findAllReports(pageIndex,pageSize);

        return reports.stream()
                .map(r -> r.toReport(r.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReport(String username, Long id){
        User admin = userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }

        Report report = reportRepository.findById(id)
                .orElseThrow(()->new BusinessException(REPORT_NOT_EXIST));


        report.deleteReport();

    }

    @Transactional
    public void deletePost(String username, Long id){
        User admin = userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }

        Post post = postRepository.findByIdAndStatus(id, Status.BLOCK)
                .orElseThrow(()-> new BusinessException(POST_NOT_EXIST));

        post.deletePost();

    }

    @Transactional
    public void restorePost(String username, Long id){
        User admin = userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }

        Post post = postRepository.findByIdAndStatus(id, Status.BLOCK)
                .orElseThrow(()-> new BusinessException(POST_NOT_EXIST));

        post.restorePost();

    }

    @Transactional
    public void deleteComment(String username, Long id){
        User admin = userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }

        Comment comment = commentRepository.findByIdAndStatus(id, Status.BLOCK)
                .orElseThrow(()-> new BusinessException(COMMENT_NOT_EXIST));

        comment.deleteComment();

    }

    @Transactional
    public void restoreComment(String username, Long id){
        User admin = userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }

        Comment comment = commentRepository.findByIdAndStatus(id, Status.BLOCK)
                .orElseThrow(()-> new BusinessException(COMMENT_NOT_EXIST));

        comment.restoreComment();

    }

    @Transactional(readOnly = true)
    public List<GetSearchUsersResponseDto> getSearchUsers(String adminId, String name, String username, String joinedDate, Status status,
                                                            int pageIndex, int pageSize){
        User admin = userRepository.findByUsernameAndStatus(adminId, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }

        if(joinedDate!=null && !Pattern.matches(regexp, joinedDate)){
            throw new BusinessException(ADMIN_INVALID_DATE);
        }

        List<User> users = queryRepository.findAllByUsers(name, username, joinedDate, status, pageIndex, pageSize);

        List<GetSearchUsersResponseDto> result = new ArrayList<>();
        users.forEach(u-> result.add(GetSearchUsersResponseDto.builder()
                    .name(u.getName())
                    .username(u.getUsername())
                    .createdAt(u.getCreatedAt().format(DateTimeFormatter.ofPattern("yy.MM.dd")))
                    .status(u.getStatus())
                    .build())
        );


        return result;

    }

    @Transactional(readOnly = true)
    public List<GetSearchPostsResponseDto> getSearchPosts(String adminId, String username, String createdDate, Status status,
                                                          int pageIndex, int pageSize){

        User admin = userRepository.findByUsernameAndStatus(adminId, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }

        if(createdDate!=null && !Pattern.matches(regexp, createdDate)){
            throw new BusinessException(ADMIN_INVALID_DATE);
        }

        List<Post> posts = queryRepository.findAllByPosts(username, createdDate, status, pageIndex, pageSize);

        List<GetSearchPostsResponseDto> result = new ArrayList<>();

        posts.forEach(p -> result.add(GetSearchPostsResponseDto.builder()
                .name(p.getUser().getName())
                .username(p.getUser().getUsername())
                .createdAt(p.getCreatedAt().format(DateTimeFormatter.ofPattern("yy.MM.dd")))
                .status(p.getStatus())
                .build()));

        return result;

    }

    @Transactional(readOnly = true)
    public GetUserInfoResponseDto getUserInfo(String adminId, String username){
        User admin = userRepository.findByUsernameAndStatus(adminId, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }

        return GetUserInfoResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .profileImgUrl(user.getProfileImgUrl())
                .introduce(user.getIntroduce())
                .website(user.getWebsite())
                .provider(user.getProvider())
                .birth(user.getBirth())
                .status(user.getStatus())
                .authority(user.getAuthority())
                .isPublic(user.isPublic)
                .build();


    }

}

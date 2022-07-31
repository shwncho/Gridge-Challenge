package com.server.insta.service;

import com.server.insta.config.Entity.Authority;
import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.User;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.server.insta.config.exception.BusinessExceptionStatus.USER_NOT_ADMIN;
import static com.server.insta.config.exception.BusinessExceptionStatus.USER_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public void test(String email){
        User admin = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }
    }



}

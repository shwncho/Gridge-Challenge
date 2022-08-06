package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.User;
import com.server.insta.log.NoLogging;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.server.insta.config.exception.BusinessExceptionStatus.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @NoLogging
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .map(this::createUser)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
    }

    private org.springframework.security.core.userdetails.User createUser(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getAuthority().toString());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.singleton(grantedAuthority));
    }


}
package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.security.jwt.JwtProvider;
import com.server.insta.dto.request.LogInRequestDto;
import com.server.insta.dto.response.LogInResponseDto;
import com.server.insta.dto.request.SignUpRequestDto;
import com.server.insta.dto.response.SignUpResponseDto;
import com.server.insta.domain.User.User;
import com.server.insta.domain.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    //회원가입
    public SignUpResponseDto signUp(SignUpRequestDto dto){
        if (userRepository.findByEmailAndStatus(dto.getEmail(), Status.ACTIVE).isPresent()) {
            log.error("이미 존재하는 이메일 입니다.");
            throw new RuntimeException("이미 존재하는 이메일 입니다.");
        }
        // 이걸 굳이 처리한 이유, 유저가 혹시 번호 양식에 맞게 입력해도 다르게 입력할 가능성을 생각해서 -> 회원가입에서 번호 제외
//        if (userRepository.findByEmailAndStatus(dto.getPhoneNumber(), Status.ACTIVE).isPresent()){
//            log.error("이미 존재하는 번호 입니다.");
//            throw new RuntimeException("이미 존재하는 번호 입니다.");
//        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User savedUser = userRepository.save(dto.toEntity());

        return new SignUpResponseDto(savedUser);
    }

    //로그인
    public LogInResponseDto signIn(LogInRequestDto dto){
        User user = userRepository.findByEmailAndStatus(dto.getEmail(), Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일 입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        UsernamePasswordAuthenticationToken authenticationToken = dto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication);

        return new LogInResponseDto(user.getId(), token);



    }

}

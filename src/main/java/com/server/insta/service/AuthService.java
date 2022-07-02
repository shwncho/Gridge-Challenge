package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.jwt.JwtProvider;
import com.server.insta.dto.LogInRequestDto;
import com.server.insta.dto.LogInResponseDto;
import com.server.insta.dto.SignUpRequestDto;
import com.server.insta.dto.SignUpResponseDto;
import com.server.insta.entity.User;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    //회원가입
    public SignUpResponseDto signUp(SignUpRequestDto dto){
        if (userRepository.findByEmailAndStatus(dto.getEmail(), Status.ACTIVE).isPresent()) {
            log.error("이미 존재하는 이메일 입니다.");
            throw new RuntimeException("이미 존재하는 이메일 입니다.");
        }
        // 이걸 굳이 처리한 이유, 유저가 혹시 번호 양식에 맞게 입력해도 다르게 입력할 가능성을 생각해서
        if (userRepository.findByEmailAndStatus(dto.getPhoneNumber(), Status.ACTIVE).isPresent()){
            log.error("이미 존재하는 번호 입니다.");
            throw new RuntimeException("이미 존재하는 번호 입니다.");
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User savedUser = userRepository.save(dto.toEntity());
        UsernamePasswordAuthenticationToken authenticationToken = dto.toAuthentication();
        SignUpResponseDto signUpResponseDto = new SignUpResponseDto(savedUser);
        signUpResponseDto.setAccess_token(jwtProvider.createToken(authenticationToken));

        return signUpResponseDto;
    }

    //로그인
    public LogInResponseDto signIn(LogInRequestDto dto){
        User user = userRepository.findByEmailAndStatus(dto.getEmail(), Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일 입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        UsernamePasswordAuthenticationToken authenticationToken = dto.toAuthentication();
        String token = jwtProvider.createToken(authenticationToken);

        return new LogInResponseDto(user.getId(), token);



    }

}

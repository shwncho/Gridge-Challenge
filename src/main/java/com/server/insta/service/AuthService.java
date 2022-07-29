package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.config.oAuth.CreateKaKaoUser;
import com.server.insta.config.response.ResponseService;
import com.server.insta.config.security.jwt.JwtProvider;
import com.server.insta.config.Entity.Provider;
import com.server.insta.dto.request.SignInRequestDto;
import com.server.insta.dto.request.SnsSignInRequestDto;
import com.server.insta.dto.response.SignInResponseDto;
import com.server.insta.dto.request.SignUpRequestDto;
import com.server.insta.dto.response.SignUpResponseDto;
import com.server.insta.domain.User;
import com.server.insta.dto.response.SnsSignInResponseDto;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    //회원가입
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto dto){
        if (userRepository.existsByEmailAndStatus(dto.getEmail(), Status.ACTIVE)) {
            throw new BusinessException(USER_EXIST_ACCOUNT);
        }

        if(userRepository.existsByNickname(dto.getNickname())){
            throw new BusinessException(USER_EXIST_NICKNAME);
        }


        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User savedUser = userRepository.save(dto.toEntity());

        return new SignUpResponseDto(savedUser);
    }

    //로그인
    @Transactional
    public SignInResponseDto signIn(SignInRequestDto dto){
        User user = userRepository.findByEmailAndStatus(dto.getEmail(), Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(USER_INVALID_PASSWORD);
        }

        UsernamePasswordAuthenticationToken authenticationToken = dto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication);

        return new SignInResponseDto(user.getId(), token);



    }

    @Transactional
    public SnsSignInResponseDto snsSignIn(SnsSignInRequestDto dto){
        SnsSignInResponseDto user;
        if(dto.getProvider().equals(Provider.KAKAO)) {
            user =CreateKaKaoUser.createKaKaoUserInfo(dto.getToken());
        }
//        다른 소셜 로그인도 구현할 경우의 로직
//        else if(dto.getProvider().equals(Provider.NAVER))   user=CreateNaverUser.createNaverUserInfo(dto.getToken());
//        else if(dto.getProvider().equals(Provider.GOOGLE))  user=CreateGoogleUser.createGoogleUserInfo(dto.getToken());
//        else if(dto.getProvider().equals(Provider.FACEBOOK))    user=CreateFacebookUser.createFacebookUserInfo(dto.getToken());
        else{
            throw new BusinessException(USER_INVALID_OAUTH);
        }

        return user;



    }

}

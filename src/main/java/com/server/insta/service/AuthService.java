package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.config.oAuth.CreateKaKaoUser;
import com.server.insta.config.security.jwt.JwtProvider;
import com.server.insta.config.Entity.Provider;
import com.server.insta.dto.request.AdminStatusRequestDto;
import com.server.insta.dto.request.SignInRequestDto;
import com.server.insta.dto.request.SnsSignInRequestDto;
import com.server.insta.dto.response.AdminStatusResponseDto;
import com.server.insta.dto.response.SignInResponseDto;
import com.server.insta.dto.request.SignUpRequestDto;
import com.server.insta.dto.response.SignUpResponseDto;
import com.server.insta.domain.User;
import com.server.insta.dto.response.SnsSignInResponseDto;
import com.server.insta.repository.QueryRepository;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final QueryRepository queryRepository;


    //회원가입
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto dto){

        //삭제 및 차단된 계정의 username도 사용못하는 조건으로 설계 -> status와 상관없이 존재여부로 판단.
        if(userRepository.existsByUsername(dto.getUsername())){
            throw new BusinessException(USER_EXIST_USERNAME);
        }



        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User savedUser = userRepository.save(dto.toEntity());

        return new SignUpResponseDto(savedUser);
    }

    //로그인
    @Transactional
    public SignInResponseDto signIn(SignInRequestDto dto){

        //소셜 로그인 일경우 email이 null이 아님
        if(dto.getEmail()!=null){
            User oAuthUser = userRepository.findByEmailAndStatus(dto.getEmail(),Status.ACTIVE)
                    .orElseThrow(()->new BusinessException(USER_NOT_EXIST));
            dto.setUsername(oAuthUser.getUsername());
        }
        User user = userRepository.findByUsernameAndStatus(dto.getUsername(), Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(USER_INVALID_PASSWORD);
        }

        //1년 주기로 개인정보동의 받기(가입시 필수 동의를 거쳐야 가입이 되므로 가입날을 기준)
        if(ChronoUnit.YEARS.between(user.getScheduler(), LocalDateTime.now())>0){
            user.resetScheduler();
            throw new BusinessException(USER_AGREE_PRIVACY);
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

    @Transactional
    public void deleteStatus(String username){
        User user = userRepository.findByUsernameAndStatus(username ,Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        user.deleteStatus();
    }

    @Transactional
    public AdminStatusResponseDto adminStatus(AdminStatusRequestDto dto){

        User admin = userRepository.findByUsernameAndStatus(dto.getAdminId(), Status.ACTIVE)
                .orElseThrow(() -> new BusinessException(USER_NOT_EXIST));

        if (!passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new BusinessException(USER_INVALID_PASSWORD);
        }

        admin.changeAuthority();

        UsernamePasswordAuthenticationToken authenticationToken = dto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication);

        return new AdminStatusResponseDto(token);

    }

}

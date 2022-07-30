package com.server.insta.service;

import com.server.insta.config.exception.BusinessException;
import com.server.insta.config.security.jwt.JwtProvider;
import com.server.insta.dto.request.AdminSignUpRequestDto;
import com.server.insta.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.server.insta.config.exception.BusinessExceptionStatus.USER_EXIST_ACCOUNT;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AdminRepository adminRepository;

    @Transactional
    public void signUp(AdminSignUpRequestDto dto){
        if(adminRepository.existsByAdminId(dto.getAdminId())){
            throw new BusinessException(USER_EXIST_ACCOUNT);
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        adminRepository.save(dto.toEntity());
    }
}

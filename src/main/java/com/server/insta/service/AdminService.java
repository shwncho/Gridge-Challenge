package com.server.insta.service;

import com.server.insta.config.Entity.Authority;
import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.Report;
import com.server.insta.domain.User;
import com.server.insta.dto.response.GetReportsResponseDto;
import com.server.insta.repository.ReportRepository;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;


    @Transactional
    public List<GetReportsResponseDto> getReports(String email){
        User admin = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }

        List<Report> reports = reportRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        return reports.stream()
                .map(r -> r.toReport(r.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReport(String email, Long id){
        User admin = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }

        Report report = reportRepository.findById(id)
                .orElseThrow(()->new BusinessException(REPORT_NOT_EXIST));


        reportRepository.delete(report);

    }

}
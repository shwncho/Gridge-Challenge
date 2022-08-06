package com.server.insta.log;

import com.server.insta.config.Entity.Authority;
import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.User;
import com.server.insta.dto.response.GetLogsResponseDto;
import com.server.insta.repository.QueryRepository;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;


@RequiredArgsConstructor
@Service
public class LogServiceImpl implements LogService{

    private final LogsRepository logsRepository;
    private final UserRepository userRepository;
    private final QueryRepository queryRepository;

    public static final String regexp = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$";

    @Override
    public void add(String text){

        logsRepository.save(Logs.builder()
                .text(text)
                .build());
    }

    @Override
    public void deleteLog(String adminId){
        User admin = userRepository.findByUsernameAndStatus(adminId, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }

        logsRepository.deleteAll();
    }



    @Transactional(readOnly = true)
    public List<GetLogsResponseDto> getLogs(String adminId, String startDate, String endDate, int pageIndex, int pageSize){
        User admin = userRepository.findByUsernameAndStatus(adminId, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(!admin.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new BusinessException(USER_NOT_ADMIN);
        }

        if(startDate!=null && !Pattern.matches(regexp, startDate)){
            throw new BusinessException(ADMIN_INVALID_DATE);
        }

        if(endDate!=null && !Pattern.matches(regexp, endDate)){
            throw new BusinessException(ADMIN_INVALID_DATE);
        }

        List<Logs> logs = queryRepository.findAllLog(startDate, endDate, pageIndex, pageSize);

        return logs.stream().map(l-> GetLogsResponseDto.builder()
                .logId(l.getId())
                .text(l.getText())
                .createdDate(l.getCreatedAt().format(DateTimeFormatter.ofPattern("yy.MM.dd")))
                .build()).collect(Collectors.toList());
    }
}

package com.server.insta.log;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class LogServiceImpl implements LogService{

    private final LogsRepository logsRepository;

    @Override
    public void add(String text){

        logsRepository.save(Logs.builder()
                .text(text)
                .build());
    }

    @Override
    public void deleteLog(){
        logsRepository.deleteAll();
    }
}

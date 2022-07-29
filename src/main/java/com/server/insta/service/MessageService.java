package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.Message;
import com.server.insta.domain.User;
import com.server.insta.dto.request.SendMessageRequestDto;
import com.server.insta.repository.MessageRepository;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.server.insta.config.exception.BusinessExceptionStatus.USER_NOT_EXIST;
import static com.server.insta.config.exception.BusinessExceptionStatus.USER_NOT_SEND_SELF;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendMessage(String email, Long id, SendMessageRequestDto dto){
        User sender = userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(()-> new BusinessException(USER_NOT_EXIST));

        User receiver = userRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(()-> new BusinessException(USER_NOT_EXIST));

        if(sender.getId() == receiver.getId()){
            throw new BusinessException(USER_NOT_SEND_SELF);
        }

        messageRepository.save(Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(dto.getContent())
                .build());
    }

}

package com.server.insta.service;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.domain.Message;
import com.server.insta.domain.User;
import com.server.insta.dto.request.SendMessageRequestDto;
import com.server.insta.dto.response.GetChattingResponseDto;
import com.server.insta.dto.response.GetMessageResponseDto;
import com.server.insta.log.NoLogging;
import com.server.insta.repository.MessageRepository;
import com.server.insta.repository.QueryRepository;
import com.server.insta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.server.insta.config.exception.BusinessExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final QueryRepository queryRepository;

    @Transactional
    public void sendMessage(String username, Long id, SendMessageRequestDto dto){
        User sender = userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
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

    @NoLogging
    @Transactional(readOnly = true)
    public GetChattingResponseDto getChatting(String username, Long id){
        User user = userRepository.findByUsernameAndStatus(username,Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));
        User otherUser = userRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(()->new BusinessException(USER_NOT_EXIST));

        if(user.getId() == otherUser.getId()){
            throw new BusinessException(USER_NOT_SEND_SELF);
        }

        List<Message> messages = queryRepository.findMessagesByUser(user, otherUser);

        if(messages.isEmpty()){
            throw new BusinessException(MESSAGE_NOT_EXIST);
        }

        List<GetMessageResponseDto> dto = new ArrayList<>();
        messages.forEach(m-> dto.add(GetMessageResponseDto.builder()
                        .sender(user.getId()==m.getSender().getId() ? "본인" : "상대방")
                        .content(m.getContent())
                        .createdAt(m.getCreatedAt().toString())
                        .build()));

        return GetChattingResponseDto.builder()
                .otherUserId(otherUser.getId())
                .profileImgUrl(otherUser.getProfileImgUrl())
                .getMessageResponseDtos(dto)
                .build();
    }

}

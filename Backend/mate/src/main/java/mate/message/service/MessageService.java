package mate.message.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import mate.chat.domain.ChatRoom;
import mate.chat.domain.ChatRoomRepository;
import mate.controller.Result;
import mate.domain.user.User;
import mate.global.exception.NotFoundException;
import mate.message.domain.ChatMessage;
import mate.message.domain.MessageQueryRepository;
import mate.message.domain.MessageRepository;
import mate.message.dto.MessageCreateRequest;
import mate.message.dto.MessagePageDto;
import mate.message.dto.MessageResponse;
import mate.repository.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static mate.global.exception.NotFoundException.CHAT_ROOM_NOT_FOUND;
import static mate.global.exception.NotFoundException.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

//    private final ChannelTopic channelTopic;
//    private final RedisTemplate redisTemplate;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageListener messageListener;
    private final MessageQueryRepository messageQueryRepository;
    private final ChatRoomRepository chatRoomRepository;


    /**
     * 메세지 생성
     */
    @Transactional
    public void createMessage(MessageCreateRequest request) throws JsonProcessingException {
        User user = userRepository.findByIdx(request.getUserIdx()).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        ChatRoom findChatRoom = chatRoomRepository.findByIdx(request.getRoomIdx())
                .orElseThrow(() -> new NotFoundException(CHAT_ROOM_NOT_FOUND));

        findChatRoom.updateTime(LocalDateTime.now());

        ChatMessage message = ChatMessage.createMessage(
                request.getRoomIdx(), request.getUserIdx(), user.getNickname(),request.getMessage(), LocalDateTime.now());
        messageRepository.save(message);
//        redisTemplate.convertAndSend(channelTopic.getTopic(), MessageResponse.from(message));
        messageListener.sendMessage(message);
    }


    public List<MessageResponse> searchMessage(Integer roomIdx, Integer messageIdx, int size) {
        List<ChatMessage> findMessages = messageQueryRepository.findTotalMessage(roomIdx, messageIdx, size);
        return findMessages.stream()
                .map(message -> MessageResponse.from(message))
                .collect(Collectors.toList());
    }

    public Result lastMessage(Integer roomIdx) {
        return messageQueryRepository.findLastMessage(roomIdx)
                .map(message -> {
                    MessageResponse response = MessageResponse.from(message);
                    return Result.builder().data(response).status(ResponseEntity.ok("마지막 메세지")).build();
                })
                .orElseGet(() -> {
                    MessageResponse response = new MessageResponse();  // 빈 응답 생성 또는 원하는 처리
                    return Result.builder().data(response).status(ResponseEntity.ok("채팅 내용 없음")).build();
                });
    }

}

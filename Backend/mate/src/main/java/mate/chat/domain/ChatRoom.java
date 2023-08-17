package mate.chat.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import mate.chat.dto.ChatRoomCreateRequest;
import mate.chat.dto.ChatRoomUpdateRequest;
import mate.domain.user.User;
import mate.global.exception.DuplicateException;
import mate.global.exception.ExistException;
import mate.global.exception.NotFoundException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private String title;

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    private int userCount;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatParticipation> chatRoomUsers = new ArrayList<>();

    public static ChatRoom createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest, User user) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.title = chatRoomCreateRequest.getTitle();
        chatRoom.type = ChatRoomStatus.LACK;
        chatRoom.createdAt = LocalDateTime.now();
        chatRoom.updatedAt = LocalDateTime.now();
        chatRoom.chatRoomUsers.add(ChatParticipation.createChatRoomUser(Role.MASTER, user, chatRoom));
        chatRoom.userCount++;
        return chatRoom;
    }
    public void update(ChatRoomUpdateRequest chatRoomUpdateRequest){
        this.title = chatRoomUpdateRequest.getTitle();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTime(LocalDateTime updatedAt){
        this.updatedAt = updatedAt;
    }

    public boolean isMaster(Integer userIdx) {
        List<ChatParticipation> result = this.chatRoomUsers.stream()
                .filter(chatRoomUser -> chatRoomUser.getUser().getIdx().equals(userIdx))
                .collect(Collectors.toList());

        return result.size() == 1 && result.get(0).getRole() == Role.MASTER;
    }

    public void addChatRoomUser(User user, LocalDateTime updatedAt) {

//        if (chatRoomUsers.size() == LIMIT) {
//            throw new NotEnoughException("채팅방 인원이 다차서 입장하실 수 없습니다.");
//        }

        List<ChatParticipation> result = chatRoomUsers.stream()
                .filter(chatRoomUser -> chatRoomUser.getUser().getIdx().equals(user.getIdx()))
                .collect(Collectors.toList());

        if (result.size() > 0) {
            throw new DuplicateException("이미 참가한 채팅방입니다.");
        }
        chatRoomUsers.add(ChatParticipation.createChatRoomUser(Role.USER, user, this));
        this.updatedAt = updatedAt;
        this.userCount++;
//        this.type = chatRoomUsers.size() == LIMIT ? ChatRoomStatus.FULL : ChatRoomStatus.LACK;
        this.type = ChatRoomStatus.LACK;
    }

    public boolean deleteChatRoomUser(User user) {
        List<ChatParticipation> result = chatRoomUsers.stream()
                .filter(chatRoomUser -> chatRoomUser.getUser().getIdx().equals(user.getIdx()))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
//            throw new NotFoundException("존재하지 않는 참가자입니다.");
            return false;
        }


        if(result.get(0).getRole() == Role.MASTER) {
//            throw new ExistException("채팅방 생성자는 채팅방을 나갈 수 없습니다.");
            return false;
        }

        chatRoomUsers.remove(result.get(0));
        this.userCount--;
//        if (chatRoomUsers.size() < LIMIT) {
//            this.status = ChatRoomStatus.LACK;
//        }
        return true;
    }


}

package mate.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mate.alarm.dto.ChatRoomResponse;
import mate.chat.dto.ChatRoomCreateRequest;
import mate.chat.dto.ChatRoomUpdateRequest;
import mate.chat.dto.ChatRoomUserRequest;
import mate.chat.service.ChatRoomService;
import mate.controller.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/list/all")
    public Result findAll(){
        return chatRoomService.findAll();
    }

    @GetMapping("/master/{roomIdx}")
    public Result findMaster(@PathVariable("roomIdx") Integer roomIdx){
        return chatRoomService.findMaster(roomIdx);
    }

    @GetMapping("/list/{userIdx}")
    public Result findByUser(@PathVariable("userIdx") Integer userIdx) {

        List<ChatRoomResponse> response = chatRoomService.findByUser(userIdx);

        return Result.builder().data(response).status(ResponseEntity.ok("채팅방 리스트")).build();
    }

    @PostMapping("/rooms")
    public Result createChatRoom(@RequestBody ChatRoomCreateRequest chatRoomCreateRequest){

        return chatRoomService.createRoom(chatRoomCreateRequest);
    }

    @DeleteMapping("/rooms/{roomIdx}")
    public Result deleteChatRoom(@PathVariable("roomIdx") Integer roomIdx,
                                @RequestParam("userIdx") Integer userIdx){

        return chatRoomService.deleteChatRoom(roomIdx, userIdx);
    }

    @PutMapping("/rooms/{roomIdx}")
    public Result updateChatRoom(@PathVariable("roomIdx") Integer roomIdx,
                                 @RequestBody ChatRoomUpdateRequest chatRoomUpdateRequest){
        return chatRoomService.updateChatRoom(roomIdx, chatRoomUpdateRequest);
    }

    @PostMapping("/rooms/{roomIdx}/users")
    public Result createChatRoomUser(@PathVariable("roomIdx") Integer roomIdx,
                                     @RequestBody ChatRoomUserRequest chatRoomUserRequest){
        return chatRoomService.createChatRoomUser(roomIdx, chatRoomUserRequest);
    }

    @DeleteMapping("/rooms/{roomIdx}/users")
    public Result deleteChatRoomUser(@PathVariable("roomIdx") Integer roomIdx,
                                     @RequestParam("userIdx") Integer userIdx){

        return chatRoomService.deleteChatRoomUser(roomIdx, userIdx);

    }

    @GetMapping("/rooms/{roomIdx}/check")
    public Result checkChatRoomUser(@PathVariable("roomIdx") Integer roomIdx,
                                    @RequestParam("userIdx") Integer userIdx){

        return chatRoomService.checkChatRoomUser(roomIdx, userIdx);
    }

    @GetMapping("/rooms/check")
    public Result checkChatRoom(@RequestParam("fromIdx") Integer fromIdx,
                                @RequestParam("toIdx") Integer toIdx){

        return chatRoomService.checkChatRoom(fromIdx, toIdx);
    }


}

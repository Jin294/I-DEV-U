package mate.alarm.service;

import lombok.RequiredArgsConstructor;
import mate.alarm.domain.Alarm;
import mate.alarm.domain.AlarmRepository;
import mate.alarm.dto.*;
import mate.chat.domain.*;
import mate.controller.Result;
import mate.domain.project.Project;
import mate.domain.user.User;
import mate.global.exception.NotFoundException;
import mate.repository.project.ProjectRepository;
import mate.repository.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static mate.global.exception.NotFoundException.CHAT_ROOM_NOT_FOUND;
import static mate.global.exception.NotFoundException.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmListener alarmListener;
    private final ProjectRepository projectRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void createAlarm(AlarmRequest request) {

        User findFromUser = userRepository.findByIdx(request.getFromIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        User findToUser = userRepository.findByIdx(request.getToIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        Alarm makeAlarm = Alarm.createAlarm(request);
        alarmRepository.saveAndFlush(makeAlarm);

        UserResponse fromUser = UserResponse.from(findFromUser);
        UserResponse toUser = UserResponse.from(findToUser);

        AlarmResponse response = AlarmResponse.from(makeAlarm, fromUser, toUser);

        alarmListener.sendAlarm(response);
    }

    @Transactional
    public void createChatAlarm(AlarmChatRequest request){

        User findFromUser = userRepository.findAll(request.getFromIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        ChatRoom findChatRoom = chatRoomRepository.findWithChatRoomUsersByIdx(request.getChatRoomIdx())
                .orElseThrow(() -> new NotFoundException(CHAT_ROOM_NOT_FOUND));

        User findToUser = findChatRoom.getChatRoomUsers().stream()
                .filter(user -> user.getRole() == Role.MASTER)
                .map(chatParticipation -> chatParticipation.getUser())
                .findFirst().orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        UserResponse fromUser = UserResponse.from(findFromUser);
        UserResponse toUser = UserResponse.from(findToUser);
        ChatRoomResponse chatRoom = ChatRoomResponse.from(findChatRoom);

        Alarm makeAlarm = Alarm.create(fromUser.getIdx(), toUser.getIdx(),
                request.getType(), LocalDateTime.now(), request.getChatRoomIdx(), request.getComment());
        alarmRepository.saveAndFlush(makeAlarm);

        AlarmResponse alarm = AlarmResponse.from(makeAlarm, fromUser, toUser);

        AlarmChatResponse response = AlarmChatResponse.from(alarm, chatRoom);
        alarmListener.sendChat(response);
    }

    @Transactional
    public Result createAlarmv2(AlarmRequest request) {

        User findFromUser = userRepository.findByIdx(request.getFromIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        User findToUser = userRepository.findByIdx(request.getToIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        Alarm makeAlarm = Alarm.createAlarm(request);
        alarmRepository.saveAndFlush(makeAlarm);

        UserResponse fromUser = UserResponse.from(findFromUser);
        UserResponse toUser = UserResponse.from(findToUser);

        AlarmResponse response = AlarmResponse.from(makeAlarm, fromUser, toUser);

        return Result.builder().data(response).status(ResponseEntity.ok("알람 응답")).build();
    }



    @Transactional
    public Result createChatAlarmv2(AlarmChatRequest request){
        System.out.println(request);
        User findFromUser = userRepository.findAll(request.getFromIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        ChatRoom findChatRoom = chatRoomRepository.findWithChatRoomUsersByIdx(request.getChatRoomIdx())
                .orElseThrow(() -> new NotFoundException(CHAT_ROOM_NOT_FOUND));

        User findToUser = findChatRoom.getChatRoomUsers().stream()
                .filter(user -> user.getRole() == Role.MASTER)
                .map(chatParticipation -> chatParticipation.getUser())
                .findFirst().orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        UserResponse fromUser = UserResponse.from(findFromUser);
        UserResponse toUser = UserResponse.from(findToUser);
        ChatRoomResponse chatRoom = ChatRoomResponse.from(findChatRoom);

        Alarm makeAlarm = Alarm.create(fromUser.getIdx(), toUser.getIdx(),
                request.getType(), LocalDateTime.now(), request.getChatRoomIdx(), request.getComment());
        alarmRepository.saveAndFlush(makeAlarm);

        AlarmResponse alarm = AlarmResponse.from(makeAlarm, fromUser, toUser);
        AlarmChatResponse response = AlarmChatResponse.from(alarm, chatRoom);
        return Result.builder().data(response).status(ResponseEntity.ok("제발")).build();
    }



    @Transactional
    public void createProjectAlarm(AlarmProjectRequest request) {

        User findFromUser = userRepository.findByIdx(request.getFromIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        Project findProject = projectRepository.findProject(request.getProjectIdx())
                .orElseThrow(() -> new NotFoundException(CHAT_ROOM_NOT_FOUND));

        User findToUser = findProject.getManager();

        Alarm makeAlarm = Alarm.create(request.getFromIdx(), findToUser.getIdx(),
                request.getType(), LocalDateTime.now(), request.getProjectIdx(), request.getComment());

        alarmRepository.saveAndFlush(makeAlarm);

        UserResponse fromUser = UserResponse.from(findFromUser);
        UserResponse toUser = UserResponse.from(findToUser);
        ProjectResponse project = ProjectResponse.from(findProject);
        AlarmResponse alarm = AlarmResponse.from(makeAlarm, fromUser, toUser);

        AlarmProjectResponse response = AlarmProjectResponse.from(alarm, project);

        alarmListener.sendProject(response);
    }


    @Transactional
    public Result createProjectAlarmv2(AlarmProjectRequest request) {
        User findFromUser = userRepository.findByIdx(request.getFromIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        Project findProject = projectRepository.findProject(request.getProjectIdx())
                .orElseThrow(() -> new NotFoundException(CHAT_ROOM_NOT_FOUND));

        User findToUser = findProject.getManager();

        Alarm makeAlarm = Alarm.create(request.getFromIdx(), findToUser.getIdx(),
                request.getType(), LocalDateTime.now(), request.getProjectIdx(), request.getComment());

        alarmRepository.saveAndFlush(makeAlarm);

        UserResponse fromUser = UserResponse.from(findFromUser);
        UserResponse toUser = UserResponse.from(findToUser);
        ProjectResponse project = ProjectResponse.from(findProject);
        AlarmResponse alarm = AlarmResponse.from(makeAlarm, fromUser, toUser);

        AlarmProjectResponse response = AlarmProjectResponse.from(alarm, project);

        return Result.builder().data(response).status(ResponseEntity.ok("알람")).build();
    }


    @Transactional
    public void createChatInviteAlarm(AlarmChatInviteRequest request) {

        User findFromUser = userRepository.findByIdx(request.getFromIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        User findToUser = userRepository.findByIdx(request.getToIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        ChatRoom findChatRoom = chatRoomRepository.findWithChatRoomUsersByIdx(request.getChatRoomIdx())
                .orElseThrow(() -> new NotFoundException(CHAT_ROOM_NOT_FOUND));

        Alarm makeAlarm = Alarm.create(request.getFromIdx(), request.getToIdx(), request.getType(),
                LocalDateTime.now(), request.getChatRoomIdx(), request.getComment());
        alarmRepository.saveAndFlush(makeAlarm);

        UserResponse fromUser = UserResponse.from(findFromUser);
        UserResponse toUser = UserResponse.from(findToUser);
        ChatRoomResponse chatRoom = ChatRoomResponse.from(findChatRoom);
        AlarmResponse alarm = AlarmResponse.from(makeAlarm, fromUser, toUser);

        AlarmChatResponse response = AlarmChatResponse.from(alarm, chatRoom);

        alarmListener.sendChatInvite(response);
    }
    @Transactional
    public Result createChatInviteAlarmv2(AlarmChatInviteRequest request) {

        User findFromUser = userRepository.findByIdx(request.getFromIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        User findToUser = userRepository.findByIdx(request.getToIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        ChatRoom findChatRoom = chatRoomRepository.findWithChatRoomUsersByIdx(request.getChatRoomIdx())
                .orElseThrow(() -> new NotFoundException(CHAT_ROOM_NOT_FOUND));

        Alarm makeAlarm = Alarm.create(request.getFromIdx(), request.getToIdx(), request.getType(),
                LocalDateTime.now(), request.getChatRoomIdx(), request.getComment());
        alarmRepository.saveAndFlush(makeAlarm);

        UserResponse fromUser = UserResponse.from(findFromUser);
        UserResponse toUser = UserResponse.from(findToUser);
        ChatRoomResponse chatRoom = ChatRoomResponse.from(findChatRoom);
        AlarmResponse alarm = AlarmResponse.from(makeAlarm, fromUser, toUser);

        AlarmChatResponse response = AlarmChatResponse.from(alarm, chatRoom);

        return Result.builder().data(response).status(ResponseEntity.ok("알림")).build();
    }

    @Transactional
    public void createProjectInviteAlarm(AlarmProjectInviteRequest request) {


        User findFromUser = userRepository.findByIdx(request.getFromIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        User findToUser = userRepository.findByIdx(request.getToIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        Project findProject = projectRepository.findProject(request.getProjectIdx())
                .orElseThrow(() -> new NotFoundException(CHAT_ROOM_NOT_FOUND));

        Alarm makeAlarm = Alarm.create(request.getFromIdx(), request.getToIdx(), request.getType(),
                LocalDateTime.now(), request.getProjectIdx(), request.getComment());
        alarmRepository.saveAndFlush(makeAlarm);

        UserResponse fromUser = UserResponse.from(findFromUser);
        UserResponse toUser = UserResponse.from(findToUser);
        ProjectResponse project = ProjectResponse.from(findProject);
        AlarmResponse alarm = AlarmResponse.from(makeAlarm, fromUser, toUser);

        AlarmProjectResponse response = AlarmProjectResponse.from(alarm, project);

        alarmListener.sendProjectInvite(response);
    }

    @Transactional
    public Result createProjectInviteAlarmv2(AlarmProjectInviteRequest request) {

        User findFromUser = userRepository.findByIdx(request.getFromIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        User findToUser = userRepository.findByIdx(request.getToIdx())
                .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

        Project findProject = projectRepository.findProject(request.getProjectIdx())
                .orElseThrow(() -> new NotFoundException(CHAT_ROOM_NOT_FOUND));

        Alarm makeAlarm = Alarm.create(request.getFromIdx(), request.getToIdx(), request.getType(),
                LocalDateTime.now(), request.getProjectIdx(), request.getComment());
        alarmRepository.saveAndFlush(makeAlarm);

        UserResponse fromUser = UserResponse.from(findFromUser);
        UserResponse toUser = UserResponse.from(findToUser);
        ProjectResponse project = ProjectResponse.from(findProject);
        AlarmResponse alarm = AlarmResponse.from(makeAlarm, fromUser, toUser);

        AlarmProjectResponse response = AlarmProjectResponse.from(alarm, project);

        return Result.builder().data(response).status(ResponseEntity.ok("알림")).build();
    }


    public Result findByFrom(Integer userIdx) {
        List<Alarm> alarmList = alarmRepository.findFrom(userIdx)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        if (alarmList.isEmpty()){
            AlarmResponse alarmResponse = new AlarmResponse();
            return Result.builder().data(alarmResponse).status(ResponseEntity.ok("받은 알림 없음")).build();
        }else{
            List<AlarmResponse> list = new ArrayList<>();
            for (Alarm alarm : alarmList) {

                User findFromUser = userRepository.findByIdx(alarm.getFromIdx())
                        .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

                User findToUser = userRepository.findByIdx(alarm.getToIdx())
                        .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));
                UserResponse fromUser = UserResponse.from(findFromUser);
                UserResponse toUser = UserResponse.from(findToUser);

                AlarmResponse response = AlarmResponse.from(alarm, fromUser, toUser);
                list.add(response);
            }
            return Result.builder().data(list).status(ResponseEntity.ok("알림")).build();
        }
    }

    public Result findByTo(Integer userIdx) {
        List<Alarm> alarmList = alarmRepository.findTo(userIdx)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        if (alarmList.isEmpty()){
            AlarmResponse alarmResponse = new AlarmResponse();
            return Result.builder().data(alarmResponse).status(ResponseEntity.ok("보낸 알림 없음")).build();
        }else{
            List<AlarmResponse> list = new ArrayList<>();
            for (Alarm alarm : alarmList) {

                User findFromUser = userRepository.findByIdx(alarm.getFromIdx())
                        .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));

                User findToUser = userRepository.findByIdx(alarm.getToIdx())
                        .orElseThrow(() -> new NotFoundException(NotFoundException.USER_NOT_FOUND));
                UserResponse fromUser = UserResponse.from(findFromUser);
                UserResponse toUser = UserResponse.from(findToUser);

                AlarmResponse response = AlarmResponse.from(alarm, fromUser, toUser);
                list.add(response);
            }
            return Result.builder().data(list).status(ResponseEntity.ok("알림")).build();
        }
    }

    @Transactional
    public Result deleteByIdx(Integer idx) {
        alarmRepository.deleteByIdx(idx);
        return Result.builder().status(ResponseEntity.ok(idx + "번 알림 삭제 성공")).build();
    }
}

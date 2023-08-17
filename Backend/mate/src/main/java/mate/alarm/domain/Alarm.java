package mate.alarm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mate.alarm.dto.AlarmProjectRequest;
import mate.alarm.dto.AlarmRequest;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private Integer fromIdx;
    private Integer toIdx;

    @Enumerated(EnumType.STRING)
    private AlarmType type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;
    private Integer targetIdx;
    private String comment;



    public static Alarm createAlarm(AlarmRequest alarmRequest){
        Alarm alarm = new Alarm();
        alarm.fromIdx = alarmRequest.getFromIdx();
        alarm.toIdx = alarmRequest.getToIdx();
        alarm.type = alarmRequest.getType();
        alarm.createdAt = LocalDateTime.now();
        alarm.comment = alarmRequest.getComment();
        return alarm;
    }
    public static Alarm create(Integer fromIdx, Integer toIdx, AlarmType type,
                               LocalDateTime time, Integer targetIdx, String comment){
        Alarm alarm = new Alarm();
        alarm.fromIdx = fromIdx;
        alarm.toIdx = toIdx;
        alarm.type = type;
        alarm.createdAt = time;
        alarm.targetIdx = targetIdx;
        alarm.comment = comment;
        return alarm;
    }

}

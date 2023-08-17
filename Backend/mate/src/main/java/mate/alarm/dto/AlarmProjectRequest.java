package mate.alarm.dto;

import lombok.Data;
import mate.alarm.domain.AlarmType;

import java.time.LocalDateTime;

@Data
public class AlarmProjectRequest {
    private Integer fromIdx;
    private AlarmType type;
//    private LocalDateTime createdAt;
    private Integer projectIdx;
    private String comment;

}


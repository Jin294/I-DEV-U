package mate.alarm.dto;

import lombok.Data;
import mate.alarm.domain.AlarmType;

import java.time.LocalDateTime;

@Data
public class AlarmProjectInviteRequest {

    private Integer fromIdx;
    private Integer toIdx;
    private AlarmType type;
//    private LocalDateTime createdAt;
    private Integer projectIdx;
    private String comment;


}

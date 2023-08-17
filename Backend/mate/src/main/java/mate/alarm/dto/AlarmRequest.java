package mate.alarm.dto;

import lombok.Data;
import mate.alarm.domain.AlarmType;

import java.time.LocalDateTime;

@Data
public class AlarmRequest{

    private Integer fromIdx;
    private Integer toIdx;

    private AlarmType type;
    private String comment;

//    private LocalDateTime createdAt;

}

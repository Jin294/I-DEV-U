package mate.dto.user;

import lombok.Data;

@Data
public class UserFollowDto {

    private Integer userIdx;
    private Integer followIdx;
    private String userName;
    private String userIntro;
    private String userNickName;
    private String storedFileName;
}

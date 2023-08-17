package mate.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mate.domain.basic.BasicAnswer;
import mate.domain.user.Role;
import mate.domain.user.User;
import mate.domain.user.UserGender;
import mate.domain.user.UserStatus;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Integer idx;
    private String name;
    private String nickname;
    private String email;
    private UserGender gender; // MALE, FEMALE
    private String intro;
    private List<BasicAnswerResponse> basicAnswerList = new ArrayList<>();
    private String storedFileName;

    public static UserResponse from(User user){

        UserResponse userResponse = new UserResponse();
        userResponse.idx = user.getIdx();
        userResponse.name = user.getName();
        userResponse.nickname = user.getNickname();
        userResponse.gender = user.getGender();
        userResponse.intro = user.getIntro();
        userResponse.email = user.getEmail();
        userResponse.basicAnswerList = BasicAnswerResponse.from(user.getBasicAnswerList());
        userResponse.storedFileName = user.getStoredFileName();

        return userResponse;
    }


}

package mate.domain.user;

import com.sun.istack.NotNull;
import lombok.*;
import mate.chat.domain.ChatParticipation;
import mate.domain.basic.BasicAnswer;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private LocalDate birth;
    @Enumerated(EnumType.STRING)
    private UserGender gender; // MALE, FEMALE
    private String intro;
    @Enumerated(EnumType.STRING)
    private UserStatus status; // A, B, C, D
    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN , USER
    private String refreshToken;
    private String invite;
    private String originalFileName;
    private String storedFileName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BasicAnswer> basicAnswerList = new ArrayList<>();

    // 생성자, 기타 메서드 생략

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
    public void setKakao(){
        this.password = "kakao";
    }

    public void setPw(String password){
        this.password = password;
    }

    public void setSetting(String invite){
        System.out.println("invite = " + invite);
        if (invite.equals("true")) this.invite = "true";
        else this.invite = "false";
    }
    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void uploadFile(String originalFileName, String storedFileName){
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
    }


}
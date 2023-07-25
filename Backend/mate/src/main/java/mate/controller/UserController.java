package mate.controller;

import mate.domain.User;
import mate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {})
public class UserController {

    @GetMapping("/sleep")
    public ResponseEntity<Map<String, String>> test(){
        Map<String, String> map = new HashMap<>();
        map.put("home", "home");
        return ResponseEntity.ok(map);
    }


    @Autowired
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    User loginUser = null;
    /*
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody Map<String, String> map) {
        Map<String, String> res = new HashMap<>();

        User user = new User();

        user.setEmail(map.get("email"));
        user.setPassword(map.get("password"));
        user.setName(map.get("name"));
        user.setNickname(map.get("nickname"));
        user.setGender(Integer.parseInt(map.get("gender")));
        user.setStatus("A");

        userService.save(user);

        return ResponseEntity.ok(res);
    }

    /*
     * 아이디 중복 확인
     */
    @GetMapping("/signup/emailcheck/{email}")
    public ResponseEntity<Map<String, String>> emailcheck(@PathVariable("email") String email) {
        Map<String, String> res = new HashMap<>();

        List<User> list = userService.emailcheck(email);

        if (list.isEmpty()) {
            res.put("resmsg", "이메일 사용 가능");
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
     * 닉네임 중복 확인
     */
    @GetMapping("/signup/nickcheck/{nickname}")
    public ResponseEntity<Map<String, String>> nickcheck(@PathVariable("nickname") String nickname) {
        Map<String, String> res = new HashMap<>();

        List<User> list = userService.nickcheck(nickname);

        if (list.isEmpty()) {
            res.put("resmsg", "닉네임 사용 가능");
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> map) {
        Map<String, Object> res = new HashMap<>();

        loginUser = userService.login(map.get("email"), map.get("password"));

        if (loginUser != null) {
            res.put("resmsg", "로그인 성공");
            res.put("user", loginUser);
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{idx}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable("idx") Integer idx) {
        Map<String, Object> res = new HashMap<>();

        if (loginUser != null) {
            res.put("resmsg", "로그인유저 호출 성공");
            res.put("user", loginUser);
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/detail")
    public ResponseEntity<Map<String, String>> modifyUser(@RequestBody Map<String, String> map) {
        Map<String, String> res = new HashMap<>();

        try {
            loginUser.setPassword(map.get("password"));
            loginUser.setName(map.get("name"));
            loginUser.setNickname(map.get("nickname"));
            loginUser.setIntro(map.get("intro"));
            userService.save(loginUser);

            res.put("resmsg", "회원정보 수정 성공");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            System.out.println("회원정보 수정 중 오류 발생");
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteUser() {
        Map<String, String> res = new HashMap<>();

        try {
            loginUser.setStatus("D");
            userService.save(loginUser);

            res.put("resmsg", "회원탈퇴 성공");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            System.out.println("회원탈퇴 진행 중 오류 발생");
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
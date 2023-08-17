package mate.controller.user;

import static org.springframework.http.ResponseEntity.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import mate.controller.Result;
import mate.domain.user.UserStatus;
import mate.dto.user.UserCheckDto;
import mate.dto.user.UserDto;
import mate.dto.user.UserFollowDto;
import mate.dto.user.UserPwDto;
import mate.dto.user.UserSettingDto;
import mate.dto.user.UserSignUpDto;
import mate.dto.user.UserUpdateDto;
import mate.repository.user.UserRepository;
import mate.service.user.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserRepository userRepository;

	@GetMapping("/sleep")
	public ResponseEntity<Map<String, String>> test() {
		Map<String, String> map = new HashMap<>();
		map.put("home", "hyeong Suck");

		return ok(map);
	}

	@GetMapping("/admin/{userIdx}")
	public ResponseEntity isAdmin(@PathVariable("userIdx") Integer userIdx){
		return userService.isAdmin(userIdx);
	}


	/*
	 * 회원가입
	 */
	@PostMapping("/signUp")
	public Result signup(@RequestBody UserSignUpDto userSignUpDto) throws Exception {
		userService.signUp(userSignUpDto);
		return Result.builder().status(ok().body("회원 가입 성공")).build();
	}

	@GetMapping("/signUp/emailCheck/{email}")
	public Result emailCheck(@PathVariable String email) {
		return userRepository.findByEmail(email)
				.map(user -> Result.builder().status(badRequest().body("이미 있는 이메일 입니다.")).build())
				.orElse(Result.builder().status(ok().body("사용가능한 이메일 입니다.")).build());
	}

	@GetMapping("/signUp/nicknameCheck/{nickname}")
	public Result nicknameCheck(@PathVariable String nickname) {
		return userRepository.findByNickname(nickname)
				.map(user -> Result.builder().status(badRequest().body("이미 있는 닉네임 입니다.")).build())
				.orElse(Result.builder().status(ok().body("사용가능한 닉네임 입니다.")).build());
	}

	@GetMapping("/check/{userIdx}")
	public Result statusCheck(@PathVariable Integer userIdx) {
		return userRepository.findByIdx(userIdx)
				.map(user -> {
					UserStatus status = user.getStatus();
					Map<String, UserStatus> map = Map.of("status", status);
					return Result.builder().status(ok().body("회원 상태 코드")).data(map).build();
				})
				.orElse(Result.builder().status(badRequest().body("존재하지 않는 회원입니다.")).build());
	}

	@GetMapping("/detail/{userIdx}")
	public Result userDetail(@PathVariable Integer userIdx) {

		return userRepository.findByIdx(userIdx).map(user -> {
			UserDto userDto = new UserDto();
			userDto.setUserIdx(user.getIdx());
			userDto.setEmail(user.getEmail());
			userDto.setName(user.getName());
			userDto.setNickname(user.getNickname());
			userDto.setBirth(user.getBirth());
			userDto.setGender(user.getGender());
			userDto.setInvite(user.getInvite());

			Optional.ofNullable(user.getIntro()).ifPresent(userDto::setIntro);
			Optional.ofNullable(user.getOriginalFileName()).ifPresent(userDto::setOriginalFileName);
			Optional.ofNullable(user.getStoredFileName()).ifPresent(userDto::setStoredFileName);

			return Result.builder().data(userDto).status(ok().body("조회 성공")).build();
		}).orElse(Result.builder().status(badRequest().body("조회 실패")).build());
	}

	@PutMapping("/modify")
	public Result userUpdate(@RequestBody UserUpdateDto userUpdateDto) throws Exception {
		return userService.update(userUpdateDto);
	}

	@DeleteMapping("/delete/{userIdx}")
	public Result userDelete(@PathVariable Integer userIdx) {
		return userRepository.findByIdx(userIdx)
				.map(user -> {
					userRepository.deleteByIdx(userIdx);
					return Result.builder().data(user.getName()).status(ok().body("삭제 성공")).build();
				})
				.orElse(Result.builder().status(ok().body("삭제 실패")).build());
	}

	@PostMapping("/modify/check")
	public Result userModifyCheck(@RequestBody UserCheckDto userCheckDto) {

		return userService.check(userCheckDto);
	}

	@PostMapping("/findPw")
	public Result userPassword(@RequestBody UserPwDto userPwDto) {
		return userRepository.findByEmailAndName(userPwDto.getEmail(), userPwDto.getName())
				.map(user -> Result.builder().status(ok().body("확인 성공")).build())
				.orElse(Result.builder().status(badRequest().body("확인 실패")).build());
	}

	@PutMapping("/findPw")
	public Result userChangePasswod(@RequestBody UserCheckDto userCheckDto) {
		return userService.changePw(userCheckDto);
	}

	@PutMapping("/setting")
	public Result userSetting(@RequestBody UserSettingDto userSettingDto) {
		return userRepository.findByIdx(userSettingDto.getUserIdx())
				.map(user -> {
					user.setSetting(userSettingDto.getInvite());
					userRepository.save(user);
					return Result.builder().status(ok().body("설정 성공")).build();
				})
				.orElse(Result.builder().status(badRequest().body("설정 실패")).build());
	}

	@GetMapping("/getFollowList/{userIdx}")
	public Result getFollowList(@PathVariable Integer userIdx) {
		return userService.getFollowList(userIdx);
	}

	@PostMapping("/follow")
	public Result userFollow(@RequestBody UserFollowDto userFollowDto) {
		return userService.follow(userFollowDto);
	}

	@DeleteMapping("/unfollow")
	public Result userUnfollow(@RequestBody UserFollowDto userFollowDto) {
		return userService.unfollow(userFollowDto);
	}

	//
	@PostMapping("/uploadFile")
	public Result uploadFile(@RequestPart(name = "file") MultipartFile multipartFile,
							 @RequestParam(name = "userIdx") Integer userIdx) throws IOException {
		return userService.uploadFile(multipartFile, userIdx);
	}

	@GetMapping("/search/email")
	public Result searchByEmail(@RequestParam("email") String email){
		return userService.searchByEmail(email);
	}

	@GetMapping("/search/nickname")
	public Result searchByNickname(@RequestParam("nickname") String nickname){
		return userService.searchByNickname(nickname);
	}

}
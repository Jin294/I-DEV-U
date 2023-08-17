package mate.controller.match;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mate.domain.match.MatchAnswer;
import mate.domain.match.MatchUser;
import mate.domain.user.User;
import mate.dto.match.MatchSurvey;
import mate.repository.match.MatchRepository;
import mate.repository.match.MatchUserRepository;
import mate.repository.user.UserRepository;
import mate.service.match.MatchService;

@RestController
@RequestMapping("/date")
@CrossOrigin(origins = {})
@RequiredArgsConstructor
public class MatchController {

	private final MatchService matchService;
	private final UserRepository userRepository;
	private final MatchRepository matchRepository;
	private final MatchUserRepository matchUserRepository;

	@GetMapping("/survey/{userIdx}")
	public ResponseEntity<Map<String, Object>> checkSurvey(@PathVariable("userIdx") int userIdx) {
		Map<String, Object> map = new HashMap<>();

		List<MatchAnswer> matchAnswer = matchRepository.findAllByUserIdx(userIdx);

		if (!matchAnswer.isEmpty()) {
			map.put("resmsg", "설문 했음");
		} else {
			map.put("resmsg", "설문 안함");
		}

		return ResponseEntity.ok(map);
	}

	@GetMapping("/{userIdx}")
	public ResponseEntity<Map<String, Object>> isRegistered(@PathVariable("userIdx") int userIdx) {
		Map<String, Object> map = new HashMap<>();

		User user = userRepository.findByIdx(userIdx).get();
		Optional<MatchUser> matchUser = matchUserRepository.findByUser(user);

		if (matchUser.isPresent()) {
			map.put("resmsg", "등록 했음");
		} else {
			map.put("resmsg", "등록 안함");
		}
		return ResponseEntity.ok(map);
	}

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> registerSurvey(@RequestBody MatchSurvey matchSurvey) {
		Map<String, Object> map = new HashMap<>();

		// try {
		matchService.registerSurvey(matchSurvey);
		map.put("resmsg", "소개팅 등록 성공");
		// } catch (Exception e) {
		//     map.put("resmsg", "소개팅 등록 실패");
		// }

		return ResponseEntity.ok(map);
	}

	@PostMapping("/modify")
	public ResponseEntity<Map<String, Object>> modifySurvey(@RequestBody MatchSurvey matchSurvey) {
		Map<String, Object> map = new HashMap<>();

		try {
			matchRepository.deleteAllByUserId(matchSurvey.getUserIdx());
			matchService.registerSurvey(matchSurvey);
			map.put("resmsg", "설문 수정 완료");
		} catch (Exception e) {
			map.put("resmsg", "설문 수정 실패");
		}

		return ResponseEntity.ok(map);
	}

	@GetMapping("/detail/{userIdx}")
	public ResponseEntity<Map<String, Object>> detailMatchUser(@PathVariable("userIdx") int userIdx) {
		Map<String, Object> map = new HashMap<>();

		try {
			map.put("user", matchService.detailMatchUser(userIdx));
			map.put("resmsg", "소개팅 유저 상세 조회 성공");
		} catch (Exception e) {
			map.put("resmsg", "소개팅 유저 상세 조회 실패");
		}

		return ResponseEntity.ok(map);
	}

	// 소개팅 사용자 등록
	@PostMapping("/register/{userIdx}")
	public ResponseEntity<Map<String, Object>> registerMatchUser(@PathVariable("userIdx") int userIdx) {
		Map<String, Object> map = new HashMap<>();

		try {
			matchService.registerMatchUser(userIdx);
			map.put("resmsg", "소개팅 등록 성공");
			// Create data map
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("userIdx", userIdx);
			map.put("data", dataMap);
		} catch (Exception e) {
			map.put("resmsg", "소개팅 등록 실패");
		}

		return ResponseEntity.ok(map);
	}

	@DeleteMapping("/release/{userIdx}")
	public ResponseEntity<Map<String, Object>> releaseMatchUser(@PathVariable("userIdx") int userIdx) {
		Map<String, Object> map = new HashMap<>();

		try {
			matchService.releaseMatchUser(userIdx);
			map.put("resmsg", "소개팅 등록해제 성공");
		} catch (Exception e) {
			map.put("resmsg", "소개팅 등록해제 실패");
		}

		return ResponseEntity.ok(map);
	}

	@GetMapping("/list/{userIdx}")
	public ResponseEntity<Map<String, Object>> listMatchUser(@PathVariable("userIdx") int userIdx) {
		Map<String, Object> map = new HashMap<>();

		try {
			map.put("user_list", matchService.listMatchUser(userIdx));
			map.put("resmsg", "소개팅 유저 리스트 조회 성공");
		} catch (Exception e) {
			map.put("resmsg", "소개팅 유저 리스트 조회 실패");
		}

		return ResponseEntity.ok(map);
	}
}

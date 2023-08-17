package mate.service.match;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mate.domain.basic.BasicAnswer;
import mate.domain.match.MatchAnswer;
import mate.domain.match.MatchUser;
import mate.domain.user.User;
import mate.dto.match.MatchDetailDto;
import mate.dto.match.MatchDto;
import mate.dto.match.MatchSurvey;
import mate.dto.user.SurveyResult;
import mate.repository.match.MatchRepository;
import mate.repository.match.MatchUserRepository;
import mate.repository.user.BasicRepository;
import mate.repository.user.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MatchService {

	private final MatchRepository matchRepository;
	private final UserRepository userRepository;
	private final MatchUserRepository matchUserRepository;
	private final BasicRepository basicRepository;

	public void registerSurvey(MatchSurvey matchSurvey) {
		User user = userRepository.findByIdx(matchSurvey.getUserIdx()).get();
		List<SurveyResult> surveyResult = matchSurvey.getSurveyResult();

		// 이미 해당 사용자가 MatchUser에 등록되어 있는지 확인
		if (!matchUserRepository.findByUser(user).isPresent()) {
			matchUserRepository.saveAndFlush(MatchUser.builder()
				.user(user)
				.build());
		}

		for (SurveyResult result : surveyResult) {
			List<String> tagList = result.getTagList();
			Integer surveyIdx = result.getSurveyIdx();
			for (String tag : tagList) {
				matchRepository.saveAndFlush(MatchAnswer.builder()
					.user(user)
					.surveyIdx(surveyIdx)
					.tag(tag)
					.build());
			}
		}
	}

	public void registerMatchUser(int userIdx) {
		User user = userRepository.findByIdx(userIdx).get();

		// 이미 해당 사용자가 MatchUser에 등록되어 있는지 확인
		MatchUser existingMatchUser = matchUserRepository.findByUser(user).get();

		// 이미 등록된 사용자
		if (existingMatchUser != null) {
			return;
		}

		matchUserRepository.saveAndFlush(MatchUser.builder()
			.user(user)
			.build());
	}

	public void releaseMatchUser(int userIdx) {
		User user = userRepository.findByIdx(userIdx).get();

		matchUserRepository.deleteMatchUserByUser(user);
	}

	public List<MatchDto> listMatchUser(int userIdx) {
		List<MatchAnswer> answers = matchRepository.findByUser(userIdx);

		User user = userRepository.findByIdx(userIdx).get();

		List<String> tag = new ArrayList<>();

		for (MatchAnswer ma : answers) {
			tag.add(ma.getTag());
		}

		List<Object> list = matchRepository.listMatchUser(tag, tag.size(), user.getGender());

		List<MatchDto> output = new ArrayList<>();

		for (Object o : list) {
			Object[] result = (Object[])o;

			// Assuming the order of elements in the array corresponds to the order of fields in PartnerDto
			Integer Idx = (Integer)result[0];
			// 자기 자신 제외
			if (Objects.equals(Idx, userIdx)) {
				continue;
			}

			String nickname = (String)result[1];
			Long percent = (Long)result[2];
			String name = (String)result[3];
			String face = (String)matchRepository.findFaceByUserIdx(Idx);

			// 생년월일 데이터베이스로부터 가져오는 부분을 시뮬레이션합니다.
			LocalDate birthDateFromDatabase = userRepository.findBirthByUserId(Idx);

			// 현재 날짜 가져오기
			LocalDate currentDate = LocalDate.now();

			Optional<String> path = userRepository.findPath(Idx);

			// 나이 계산
			Period agePeriod = Period.between(birthDateFromDatabase, currentDate);
			int age = agePeriod.getYears();

			MatchDto matchDto = new MatchDto();
			matchDto.setUserIdx(Idx);
			matchDto.setName(name);
			matchDto.setNickname(nickname);
			matchDto.setFace(face);
			matchDto.setPercent(percent);
			matchDto.setAge(age);
			path.ifPresent(matchDto::setStoredFileName);

			output.add(matchDto);

			System.out.println(matchDto);
		}

		return output;
	}

	public MatchDetailDto detailMatchUser(int userIdx) {
		User user = userRepository.findByIdx(userIdx).get();
		List<MatchAnswer> matchAnswer = matchRepository.findAllByUserIdx(userIdx);
		List<BasicAnswer> basicAnswer = basicRepository.findByUser(userIdx);

		System.out.println(basicAnswer.size());

		MatchDetailDto matchDetailDto = new MatchDetailDto();

		String nickname = user.getNickname();
		String work = basicAnswer.get(1).getTag();
		List<String> language = basicRepository.findLanguage(userIdx);
		String location = basicAnswer.get(3).getTag();
		String face = matchAnswer.get(0).getTag();
		String job = basicAnswer.get(0).getTag();
		// 생년월일 데이터베이스로부터 가져오는 부분을 시뮬레이션합니다.
		LocalDate birthDateFromDatabase = user.getBirth(); // 생년월일 설정
		// 현재 날짜 가져오기
		LocalDate currentDate = LocalDate.now();
		// 나이 계산
		Period agePeriod = Period.between(birthDateFromDatabase, currentDate);
		int age = agePeriod.getYears();
		String salary = matchAnswer.get(1).getTag();
		String gender = String.valueOf(user.getGender());
		String intro = user.getIntro();

		// MatchDetailDto에 값 설정
		matchDetailDto.setNickname(nickname);
		matchDetailDto.setWork(work);
		matchDetailDto.setLanguage(language);
		matchDetailDto.setLocation(location);
		matchDetailDto.setFace(face);
		matchDetailDto.setJob(job);
		matchDetailDto.setAge(age);
		matchDetailDto.setSalary(salary);
		matchDetailDto.setGender(gender);
		matchDetailDto.setIntro(intro);
		matchDetailDto.setStoredFileName(user.getStoredFileName());

		return matchDetailDto;
	}
}

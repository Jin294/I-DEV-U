package mate.repository.match;

import java.util.List;

import mate.domain.user.UserGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mate.domain.match.MatchAnswer;

@Repository
public interface MatchRepository extends JpaRepository<MatchAnswer, Integer> {
	@Query("select m from MatchAnswer m where m.user.idx = :userIdx "
		+ "and (m.surveyIdx >= 4 AND m.surveyIdx <= 16)")
	List<MatchAnswer> findByUser(@Param("userIdx") Integer userIdx);

	@Query("select m.tag from MatchAnswer m where m.user.idx = :userIdx and m.surveyIdx = 1")
	Object findFaceByUserIdx(@Param("userIdx") Integer userIdx);

	@Query("select m from MatchAnswer m where m.user.idx = :userIdx")
	List<MatchAnswer> findAllByUserIdx(@Param("userIdx") Integer userIdx);

	@Modifying
	@Transactional
	@Query("delete from MatchAnswer m where m.user.idx = :userIdx")
	void deleteAllByUserId(@Param("userIdx") Integer userIdx);

	// 내가 원하는 상대방에 관한 설문을 바탕으로 상대방 유저의 설문과 비교함
	@Query(
		"SELECT ma.user.idx as userIdx, ma.user.nickname as nickname, ROUND((COUNT(*) / :size) * 100) AS percent, ma.user.name as name " +
			"FROM MatchAnswer ma join MatchUser mu ON mu.user.idx = ma.user.idx " +
			"WHERE ma.tag IN (:tag) and (ma.surveyIdx != 4 and ma.surveyIdx != 5 and ma.surveyIdx != 6) and (ma.user.gender != :gender) " +
			"GROUP BY ma.user.idx " +
			"ORDER BY percent DESC")
	List<Object> listMatchUser(@Param("tag") List<String> tag, @Param("size") long size, @Param("gender") UserGender gender);
}

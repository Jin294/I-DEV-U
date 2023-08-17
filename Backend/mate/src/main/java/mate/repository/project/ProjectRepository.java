package mate.repository.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mate.domain.project.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
	@Query(value = "select p from Project as p where p.title like %:keyword% or p.content like %:keyword%")
	List<Project> findProjectsByTitleOrContent(@Param(value = "keyword") String keyword);

	@Modifying
	@Query("UPDATE Project p SET p.nowNum = p.nowNum+1 WHERE p.idx = :projectIdx")
	void plusnowNum(@Param("projectIdx") int projectIdx);

	@Modifying
	@Query("UPDATE Project p SET p.nowNum = p.nowNum-1 WHERE p.idx = :projectIdx")
	void minusnowNum(@Param("projectIdx") int projectIdx);

	@Query("select p from Project p where p.idx = :projectIdx")
	Optional<Project> findProject(@Param("projectIdx") Integer projectIdx);

	@Query("select distinct p from Project p join fetch p.projectParticipation where p.idx = :projectIdx")
	Optional<Project> findProjectUsers(@Param("projectIdx") Integer projectIdx);

	@Query("select DISTINCT p from Project p " +
		"JOIN p.projectLanguages pl " +
		"where p.type = :type " +
		"AND pl.language IN (:language)")
		// "AND ((:position = 'back' AND p.back < p.max_back) OR (:position = 'front' AND p.front < p.max_front))")
	List<Project> findProjectsByFilter(@Param("language") List<String> language, @Param("type") String type);
}

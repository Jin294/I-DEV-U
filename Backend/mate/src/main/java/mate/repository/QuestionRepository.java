package mate.repository;

import mate.domain.question.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionBoard, Integer> {
    List<QuestionBoard> findByTitleContainingOrderByIdxDesc(String keyWord);
    List<QuestionBoard> findByContentContainingOrderByIdxDesc(String keyWord);
//    List<QuestionBoard> findByNameOrderByIdxDesc(String name);

    @Query("select q from QuestionBoard q join fetch q.user u where u.nickname like %:name%")
    List<QuestionBoard> findByNameOrderByIdxDesc(String name);
    List<QuestionBoard> findTop4ByOrderByIdxDesc();

}

package mate.repository.project;

import mate.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query(value = "select p from Project as p where p.title like %:keyword% or p.content like %:keyword%")
    List<Project> findProjectsByTitleOrContent(@Param(value = "keyword") String keyword);
}
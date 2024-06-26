package aChaushev.architects.repository;

import aChaushev.architects.model.entity.Project;
import aChaushev.architects.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByArchitect(User user);

    List<Project> findByArchitectIsNot(User user);

    @Query("SELECT p FROM Project p WHERE p.isFavorite IS TRUE ")
    List<Project> findByisFavoriteIsTrue(User user);
}

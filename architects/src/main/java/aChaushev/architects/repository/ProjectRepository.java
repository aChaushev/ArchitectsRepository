package aChaushev.architects.repository;

import aChaushev.architects.model.entity.Project;
import aChaushev.architects.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByArchitect(User user);

    List<Project> findByArchitectIsNot(User user);

    Optional<Project> findByName(String name);
}

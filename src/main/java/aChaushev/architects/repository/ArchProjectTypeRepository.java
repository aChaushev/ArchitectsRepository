package aChaushev.architects.repository;

import aChaushev.architects.model.entity.ArchProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchProjectTypeRepository extends JpaRepository<ArchProjectType, Long> {
}

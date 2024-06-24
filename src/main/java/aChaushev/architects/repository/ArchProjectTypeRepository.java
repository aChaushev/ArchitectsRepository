package aChaushev.architects.repository;

import aChaushev.architects.model.entity.ArchProjectType;
import aChaushev.architects.model.enums.ArchProjectTypeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchProjectTypeRepository extends JpaRepository<ArchProjectType, Long> {
    ArchProjectType findByProjectTypeName(ArchProjectTypeName typeName);
}

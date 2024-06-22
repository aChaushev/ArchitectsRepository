package aChaushev.architects.init;


import aChaushev.architects.model.entity.ArchProjectType;
import aChaushev.architects.model.enums.ArchProjectTypeName;
import aChaushev.architects.repository.ArchProjectTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class ArchProjectTypeInit implements CommandLineRunner {

    private final ArchProjectTypeRepository archProjectTypeRepository;

    public ArchProjectTypeInit(ArchProjectTypeRepository archProjectTypeRepository) {
        this.archProjectTypeRepository = archProjectTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        boolean hasLanguages = archProjectTypeRepository.count() > 0;

        if (!hasLanguages) {

            List<ArchProjectType> projectTypes = new ArrayList<>();

            Arrays.stream(ArchProjectTypeName.values())
                    .forEach(projectTypeName -> {
                        ArchProjectType projectType = new ArchProjectType();
                        projectType.setProjectTypeName(projectTypeName);
                        projectTypes.add(projectType);
                    });

            archProjectTypeRepository.saveAll(projectTypes);
        }
    }
}


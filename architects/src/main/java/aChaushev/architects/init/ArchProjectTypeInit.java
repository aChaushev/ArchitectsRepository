package aChaushev.architects.init;

import aChaushev.architects.model.entity.ArchProjectType;
import aChaushev.architects.model.enums.ArchProjectTypeName;
import aChaushev.architects.repository.ArchProjectTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ArchProjectTypeInit implements CommandLineRunner {

    private final ArchProjectTypeRepository archProjectTypeRepository;

    public ArchProjectTypeInit(ArchProjectTypeRepository archProjectTypeRepository) {
        this.archProjectTypeRepository = archProjectTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (archProjectTypeRepository.count() == 0) {
            Arrays.stream(ArchProjectTypeName.values())
                    .forEach(projectTypeName -> {
                        if (archProjectTypeRepository.findByProjectTypeName(projectTypeName) == null) {
                            ArchProjectType projectType = new ArchProjectType();
                            projectType.setProjectTypeName(projectTypeName);
                            projectType.setDescription(projectTypeName); // This sets the description based on the enum
                            archProjectTypeRepository.save(projectType);
                        }
                    });
        }
    }
}

package aChaushev.architects.model.entity;

import aChaushev.architects.model.enums.ArchProjectTypeName;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "project_types")
public class ArchProjectType extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private ArchProjectTypeName projectTypeName;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "archProjectType")
    private Set<Project> project;

    public ArchProjectTypeName getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(ArchProjectTypeName projectTypeName) {
        this.projectTypeName = projectTypeName;
        this.setDescription(projectTypeName);
    }

    public Set<Project> getProject() {
        return project;
    }

    public void setProject(Set<Project> project) {
        this.project = project;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescription(ArchProjectTypeName archProjectTypeName) {
        String description = "";

        switch (archProjectTypeName) {
            case RESIDENTIAL -> description =
                    "Residential architects design homes, working with homeowners to design a custom home or adjust the design or layout of an existing home.";
            case COMMERCIAL -> description =
                    "A commercial architect designs buildings for commercial purposes, such as skyscrapers, large office buildings, condos, and hotels, as well as bridges, schools and museums.";
            case LANDSCAPE -> description =
                    "Landscape architects work on creating beautiful outdoor spaces as opposed to commercial properties or entire homes. Such spaces might include parks, college campuses, and garden areas.";
            case INTERIOR -> description =
                    "Interior design architects work on the inside of buildings, getting the most out of both big and small spaces, using knowledge of color theory, function and feel of the materials";
            case URBAN -> description =
                    "Urban design architect takes on the challenge of building for a much larger space, such as an entire block of houses or a whole town.";
        }

        this.description = description;
    }

}

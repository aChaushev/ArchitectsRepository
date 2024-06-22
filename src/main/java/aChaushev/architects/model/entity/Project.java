package aChaushev.architects.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate inputDate;

    @ManyToOne
    private ArchProjectType archProjectType;

    @ManyToOne
    private User architect;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getInputDate() {
        return inputDate;
    }

    public void setInputDate(LocalDate inputDate) {
        this.inputDate = inputDate;
    }

    public ArchProjectType getArchProjectType() {
        return archProjectType;
    }

    public void setArchProjectType(ArchProjectType archProjectType) {
        this.archProjectType = archProjectType;
    }

    public User getArchitect() {
        return architect;
    }

    public void setArchitect(User architect) {
        this.architect = architect;
    }
}

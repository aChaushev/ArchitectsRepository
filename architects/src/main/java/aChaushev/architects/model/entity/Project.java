package aChaushev.architects.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
//    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDate inputDate;

    @Column(nullable = false)
    private String imageURL;

    @Column(nullable = false, columnDefinition="tinyint(0) default 0" )
    private boolean isFavorite = false;

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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String image) {
        this.imageURL = image;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}


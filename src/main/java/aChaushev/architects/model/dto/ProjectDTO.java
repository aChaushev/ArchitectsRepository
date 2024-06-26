package aChaushev.architects.model.dto;

import aChaushev.architects.model.entity.User;
import aChaushev.architects.model.enums.ArchProjectTypeName;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProjectDTO {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private LocalDate inputDate;

    private String imageURL;

    private ArchProjectTypeName typeName;

    private User architect;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getInputDate() {
        return inputDate;
    }

    public void setInputDate(LocalDate inputDate) {
        this.inputDate = inputDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ArchProjectTypeName getTypeName() {
        return typeName;
    }

    public void setTypeName(ArchProjectTypeName typeName) {
        this.typeName = typeName;
    }

    public User getArchitect() {
        return architect;
    }

    public void setArchitect(User architect) {
        this.architect = architect;
    }
}

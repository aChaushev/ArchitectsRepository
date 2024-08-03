package aChaushev.architects.model.dto;

import aChaushev.architects.model.entity.Project;
import jakarta.validation.constraints.*;


import java.math.BigDecimal;

public class OfferAddDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Creation date is required")
    private String createdOn;

    @NotBlank(message = "Valid until date is required")
    private String validUntil;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Description is required")
    private String description;

    private Project project;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}

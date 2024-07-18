package aChaushev.architects.model.dto;


import aChaushev.architects.model.entity.Project;

import java.math.BigDecimal;

public class OfferAddDTO {

    private String id;
    private String createdOn;
    private String ValidUntil;
    private BigDecimal price;
    private String description;
    private Project project;

    public String getId() {
        return id;
    }

    public OfferAddDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public OfferAddDTO setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public String getValidUntil() {
        return ValidUntil;
    }

    public OfferAddDTO setValidUntil(String validUntil) {
        ValidUntil = validUntil;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OfferAddDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public OfferAddDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public OfferAddDTO setProject(Project project) {
        this.project = project;
        return this;
    }
}

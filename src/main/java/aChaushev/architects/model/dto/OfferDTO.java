package aChaushev.architects.model.dto;


import aChaushev.architects.model.entity.Project;

import java.math.BigDecimal;

public class OfferDTO {

    private String id;
    private String createdOn;
    private String validUntil;
    private BigDecimal price;
    private String description;
    private Project project;

    public String getId() {
        return id;
    }

    public OfferDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public OfferDTO setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public OfferDTO setValidUntil(String validUntil) {
        this.validUntil = validUntil;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OfferDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public OfferDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public OfferDTO setProject(Project project) {
        this.project = project;
        return this;
    }
}

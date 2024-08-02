package bg.softuni.arch_repo.architects_offers.model.dto;

import java.math.BigDecimal;

public class OfferDTO {

    private String id;
    private String name;
    private String createdOn;
    private String validUntil;
    private BigDecimal price;
    private String description;

    public String getId() {
        return id;
    }

    public OfferDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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



}


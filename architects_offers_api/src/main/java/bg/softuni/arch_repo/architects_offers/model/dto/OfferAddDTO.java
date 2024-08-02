package bg.softuni.arch_repo.architects_offers.model.dto;

import java.math.BigDecimal;

public class OfferAddDTO {

    private String name;
    private String createdOn;
    private String validUntil;
    private BigDecimal price;
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public OfferAddDTO setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public OfferAddDTO setValidUntil(String validUntil) {
        this.validUntil = validUntil;
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


}

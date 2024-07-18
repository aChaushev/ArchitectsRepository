package aChaushev.architects.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id",
            referencedColumnName = "id")
    private User user;

    @Column(name = "created_on", nullable = false)
    private String createdOn;

    @Column(name = "valid_until", nullable = false)
    private String validUntil;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id",
    referencedColumnName = "id")
    private Project project;

    public User getUser() {
        return user;
    }

    public Offer setUser(User user) {
        this.user = user;
        return this;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public Offer setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public Offer setValidUntil(String validUntil) {
        this.validUntil = validUntil;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Offer setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Offer setDescription(String description) {
        this.description = description;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public Offer setProject(Project project) {
        this.project = project;
        return this;
    }
}

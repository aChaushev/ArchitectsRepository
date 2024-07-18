package aChaushev.architects.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class Event extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "date", nullable = false)
    private String date;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "username",
            referencedColumnName = "username")
    private User user;
}

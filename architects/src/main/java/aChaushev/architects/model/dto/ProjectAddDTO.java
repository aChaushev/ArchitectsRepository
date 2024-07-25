package aChaushev.architects.model.dto;

import aChaushev.architects.model.enums.ArchProjectTypeName;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProjectAddDTO {

    @NotBlank(message = "Project name is required!")
    @Size(min = 5, max = 40, message = "Project name length must be between 5 and 40 characters!")
    private String name;

    @NotBlank(message = "{add.project.description.not.empty}")
    @Size(min = 2, max = 300, message = "{add.project.description.length}")
    private String description;

    @NotNull(message = "Project price is required!")
    @DecimalMin(value = "1000", message = "Project price must be minimum 1000 USD!")
    private BigDecimal price;

    @NotNull(message = "Project input date required!")
    @PastOrPresent(message = "The input date must be in past or present!")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate inputDate;

    @NotNull
    @Size(min = 1 ,message = "Project image URL is required.")
    @Size(max = 300, message = "Image URL must be no more than 300 characters!")
    private String imageURL;

    @NotNull(message = "Select project type!")
    private ArchProjectTypeName typeName;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


}



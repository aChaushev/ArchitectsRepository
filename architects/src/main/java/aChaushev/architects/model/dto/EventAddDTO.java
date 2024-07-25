package aChaushev.architects.model.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class EventAddDTO {

    @NotBlank(message = "Event name is required!")
    @Size(min = 5, max = 40, message = "Project name length must be between 5 and 40 characters!")
    private String name;

    @Size(max = 300, message = "Image URL must be no more than 300 characters!")
    private String imageURL;

    @NotBlank(message = "Event description is required.")
    @Size(min = 2, max = 300, message = "Description length must be between 2 and 300 symbols.")
    private String description;

    @NotNull(message = "Project input date required!")
    @Future(message = "The input date must be in the future!")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate inputDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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
}

package aChaushev.architects.model.dto;

import aChaushev.architects.model.validation.annotation.FieldMatch;
import aChaushev.architects.model.validation.annotation.UniqueUserEmail;
import aChaushev.architects.model.validation.annotation.UniqueUsername;
import jakarta.validation.constraints.*;

///TODO: @FieldMatch not working
//@FieldMatch(
//        first = "password",
//        second = "confirmPassword",
//        message = "Passwords should match.")
public class UserRegisterDTO {


    @Size(min = 3,max = 20,message = "Username length must be between 3 and 20 characters!")
    @NotNull
    @UniqueUsername
    private String username;

    @Email
    @NotBlank(message = "Email can not be empty")
    @UniqueUserEmail
    private String email;

    @Size(min = 3,max = 20,message = "Password length must be between 3 and 20 characters!")
    @NotNull
    private String password;

    @NotBlank
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}

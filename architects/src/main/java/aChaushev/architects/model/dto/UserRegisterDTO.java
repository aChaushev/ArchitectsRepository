package aChaushev.architects.model.dto;

import aChaushev.architects.validation.annotation.*;
import jakarta.validation.constraints.*;

@ValidatePasswords(message = "{register.user.error_msg.passwords.validator}")
public class UserRegisterDTO {


    @Size(min = 3,max = 20,message = "{register.user.error_msg.username.length}")
    @NotNull
    @UniqueUsername(message = "{register.user.error_msg.username.unique}")
    private String username;

    @Email
    @NotBlank(message = "{register.form.error_msg.email.not_blank}")
    @UniqueUserEmail(message = "{register.user.error_msg.email.unique}")
    private String email;

    @Size(min = 3,max = 20,message = "{register.form.error_msg.password.length}")
    @NotNull
    private String password;

    @NotBlank(message = "{register.form.error_msg.confirm_password.not_blank}")
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

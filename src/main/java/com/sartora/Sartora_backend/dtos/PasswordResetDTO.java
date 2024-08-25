package com.sartora.Sartora_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter

public class PasswordResetDTO {

    @NonNull
    @NotBlank
    private String token;

    @NonNull
    @NotBlank
    @Size(min = 6, max = 32)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 8 characters long")
    private String password;


}

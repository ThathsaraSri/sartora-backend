package com.sartora.Sartora_backend.dtos;

import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data

public class RegistrationDTO {


    @NonNull
    @Size(min = 8, max = 30)
    private String username;
    @NonNull
    @Email
    private String email;
    @NonNull
    @NotBlank
    @Size(min = 6, max = 32)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 8 characters long")
    private String password;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;

}

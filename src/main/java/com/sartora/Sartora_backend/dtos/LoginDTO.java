package com.sartora.Sartora_backend.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter

public class LoginDTO {
    @NonNull
    @NotBlank
    private String username;
    @NonNull
    @NotBlank
    private String password;
}

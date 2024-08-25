package com.sartora.Sartora_backend.dtos;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class LoginResponseDTO {
    private String jwt;
    private boolean success;
    private String failureReason;
}



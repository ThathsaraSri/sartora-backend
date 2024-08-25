package com.sartora.Sartora_backend.controller.registration;

import com.sartora.Sartora_backend.dtos.LoginDTO;
import com.sartora.Sartora_backend.dtos.LoginResponseDTO;
import com.sartora.Sartora_backend.dtos.PasswordResetDTO;
import com.sartora.Sartora_backend.dtos.RegistrationDTO;
import com.sartora.Sartora_backend.entity.LocalUser;
import com.sartora.Sartora_backend.exception.EmailFailureException;
import com.sartora.Sartora_backend.exception.EmailNotFoundException;
import com.sartora.Sartora_backend.exception.UserAlreadyExistsException;
import com.sartora.Sartora_backend.exception.UserNotVerifiedException;
import com.sartora.Sartora_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

    @Autowired
    private UserService userService;



    @PostMapping("register")
    public ResponseEntity<RegistrationDTO> registerUser(@Valid @RequestBody RegistrationDTO registrationDTO){

        try {
            userService.registerUser(registrationDTO);
            return ResponseEntity.ok().build();
        }catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EmailFailureException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginDTO loginDTO){
        String jwt = null;
        try {
            jwt = userService.loginUser(loginDTO);
        } catch (EmailFailureException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (UserNotVerifiedException ex) {
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setSuccess(false);
        String reason = "USER_NOT_VERIFIED";
            if(ex.isNewEmailSent()){
                reason += "_EMAIL_IS RESENT";
            }
            loginResponseDTO.setFailureReason(reason);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(loginResponseDTO);
        }
        if(jwt != null){
            LoginResponseDTO response= new LoginResponseDTO();
            response.setJwt(jwt);
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user){
        return user;
    }
    @PostMapping("/verify")
    public ResponseEntity verifyEmail(@RequestParam String token){
        if(userService.verifyUser(token)){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }
    @PostMapping("/forgot")
    public ResponseEntity forgotPassword(@RequestParam String email){
        try {
            userService.forgotPassword(email);
            return ResponseEntity.ok().build();
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/reset")
    public ResponseEntity resetPassword(@Valid @RequestBody PasswordResetDTO passwordResetDTO){
        userService.resetPassword(passwordResetDTO);
        return ResponseEntity.ok().build();
    }
}

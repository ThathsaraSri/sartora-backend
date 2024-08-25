package com.sartora.Sartora_backend.service;

import com.sartora.Sartora_backend.dtos.LoginDTO;
import com.sartora.Sartora_backend.dtos.RegistrationDTO;
import com.sartora.Sartora_backend.entity.LocalUser;
import com.sartora.Sartora_backend.entity.VerificationToken;
import com.sartora.Sartora_backend.exception.EmailNotFoundException;
import com.sartora.Sartora_backend.exception.UserAlreadyExistsException;
import com.sartora.Sartora_backend.repository.LocalUserRepository;
import com.sartora.Sartora_backend.repository.VerificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private LocalUserRepository localUserRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JWTService jwtService;

    @Mock
    private EmailService emailService;

    @Mock
    private VerificationRepository verificationRepository;

    @InjectMocks
    private UserService userService;

    private RegistrationDTO registrationDTO;

    @BeforeEach
    void setup() {
        registrationDTO = new RegistrationDTO();
        registrationDTO.setEmail("test@example.com");
        registrationDTO.setUsername("testuser");
        registrationDTO.setPassword("password123");
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        when(localUserRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(new LocalUser()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(registrationDTO));

        verify(localUserRepository, never()).save(any(LocalUser.class));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        when(localUserRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(localUserRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(encryptionService.encryptPassword(anyString())).thenReturn("encryptedPassword");
        when(modelMapper.map(any(RegistrationDTO.class), eq(LocalUser.class))).thenReturn(new LocalUser());
        when(jwtService.generateVerificationJWT(any(LocalUser.class))).thenReturn("verificationToken");
        doNothing().when(emailService).sendVerificationEmail(any(VerificationToken.class));
        when(localUserRepository.save(any(LocalUser.class))).thenReturn(new LocalUser());

        RegistrationDTO result = userService.registerUser(registrationDTO);

        assertEquals("test@example.com", result.getEmail());
        verify(localUserRepository, times(2)).save(any(LocalUser.class));
        verify(emailService, times(1)).sendVerificationEmail(any(VerificationToken.class));
    }

    @Test
    void testLoginUser_Success() throws Exception {
        LocalUser user = new LocalUser();
        user.setPassword("encryptedPassword");
        user.setEmailVerified(true);

        when(localUserRepository.findByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(encryptionService.checkPassword(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateJWT(any(LocalUser.class))).thenReturn("jwtToken");

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");

        String token = userService.loginUser(loginDTO);

        assertEquals("jwtToken", token);
    }

    @Test
    void testForgotPassword_EmailNotFound() {
        when(localUserRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> userService.forgotPassword("test@example.com"));
    }

    @Test
    void testForgotPassword_Success() throws Exception {
        LocalUser user = new LocalUser();
        when(localUserRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generatePasswordResetJWT(any(LocalUser.class))).thenReturn("resetToken");

        userService.forgotPassword("test@example.com");

        verify(emailService, times(1)).sendPasswordResetEmail(any(LocalUser.class), anyString());
    }

    @Test
    void testVerifyUser_Success() {
        LocalUser user = new LocalUser();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);

        when(verificationRepository.findByToken(anyString())).thenReturn(Optional.of(verificationToken));

        boolean result = userService.verifyUser("token");

        assertTrue(result);
        verify(verificationRepository, times(1)).deleteByUser(any(LocalUser.class));
    }
}

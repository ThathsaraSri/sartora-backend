package com.sartora.Sartora_backend.service;

import com.sartora.Sartora_backend.dtos.LoginDTO;
import com.sartora.Sartora_backend.dtos.PasswordResetDTO;
import com.sartora.Sartora_backend.dtos.RegistrationDTO;
import com.sartora.Sartora_backend.entity.LocalUser;
import com.sartora.Sartora_backend.entity.VerificationToken;
import com.sartora.Sartora_backend.exception.EmailFailureException;
import com.sartora.Sartora_backend.exception.EmailNotFoundException;
import com.sartora.Sartora_backend.exception.UserAlreadyExistsException;
import com.sartora.Sartora_backend.exception.UserNotVerifiedException;
import com.sartora.Sartora_backend.repository.LocalUserRepository;
import com.sartora.Sartora_backend.repository.VerificationRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class UserService {


    private final EncryptionService encryptionService;
    private final LocalUserRepository localUserRepository;
    private final ModelMapper modelMapper;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final VerificationRepository verificationRepository;

    public UserService(EncryptionService encryptionService, LocalUserRepository localUserRepository, ModelMapper modelMapper, JWTService jwtService, EmailService emailService, VerificationRepository verificationRepository) {
        this.encryptionService = encryptionService;
        this.localUserRepository = localUserRepository;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.verificationRepository = verificationRepository;
    }

    public RegistrationDTO registerUser(RegistrationDTO registrationDTO)
            throws UserAlreadyExistsException, EmailFailureException {

        if (localUserRepository.findByEmailIgnoreCase(registrationDTO.getEmail()).isPresent()
                || localUserRepository.findByUsernameIgnoreCase(registrationDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        String encryptedPassword = encryptionService.encryptPassword(registrationDTO.getPassword());
        registrationDTO.setPassword(encryptedPassword);
        LocalUser user = modelMapper.map(registrationDTO, LocalUser.class);
        LocalUser savedUser = localUserRepository.save(user);
        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        verificationRepository.save(verificationToken);
        localUserRepository.save(user);
        return registrationDTO;
    }

    private VerificationToken createVerificationToken(LocalUser user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

    public String loginUser(LoginDTO loginDTO) throws EmailFailureException, UserNotVerifiedException {
        Optional<LocalUser> opUser = localUserRepository.findByUsernameIgnoreCase(loginDTO.getUsername());

        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            if (encryptionService.checkPassword(loginDTO.getPassword(), user.getPassword())) {
                if (user.getEmailVerified()) {
                    return jwtService.generateJWT(user);
                } else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.size() == 0 || verificationTokens.get(0).getCreateTimestamp()
                            .before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    if (resend) {
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationRepository.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        return null;
    }
    @Transactional
    public boolean verifyUser(String token) {

        Optional<VerificationToken> opToken = verificationRepository.findByToken(token);
        if (opToken.isPresent()) {
            VerificationToken verificationToken = opToken.get();
            LocalUser user = verificationToken.getUser();
            if(!user.getEmailVerified()) {
                user.setEmailVerified(true);
                verificationRepository.save(verificationToken);
                verificationRepository.deleteByUser(user);
                return true;
            }
        }return false;

    }
    public void forgotPassword(String email) throws EmailNotFoundException, EmailFailureException {
        Optional<LocalUser> opUser = localUserRepository.findByEmailIgnoreCase(email);
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            String token =jwtService.generatePasswordResetJWT(user);
            emailService.sendPasswordResetEmail(user, token);



        }else {
            throw new EmailNotFoundException();
        }

    }
    public void resetPassword(PasswordResetDTO passwordResetDTO ) {
        String email = jwtService.getResetPasswordEmail(passwordResetDTO.getToken());
        Optional<LocalUser> opUser = localUserRepository.findByEmailIgnoreCase(email);
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            user.setPassword(encryptionService.encryptPassword(passwordResetDTO.getPassword()));
            localUserRepository.save(user);
        }



    }
}

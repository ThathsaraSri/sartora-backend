package com.sartora.Sartora_backend.repository;


import com.sartora.Sartora_backend.entity.LocalUser;
import com.sartora.Sartora_backend.entity.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;


public interface LocalUserRepository extends ListCrudRepository <LocalUser, Long> {
    Optional<LocalUser> findByUsernameIgnoreCase(String username);
    Optional<LocalUser> findByEmailIgnoreCase(String email);

}

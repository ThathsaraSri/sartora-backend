package com.sartora.Sartora_backend.repository;

import com.sartora.Sartora_backend.entity.LocalUser;
import com.sartora.Sartora_backend.entity.Product;
import com.sartora.Sartora_backend.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface VerificationRepository extends ListCrudRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
    void deleteByUser(LocalUser user);
}

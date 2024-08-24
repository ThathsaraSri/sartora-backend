package com.sartora.Sartora_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "verification_token")
@Getter
@Setter

public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;

    @Lob
    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "create_timestamp", nullable = false)
    private Timestamp createTimestamp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "local_user_id", nullable = false)
    private LocalUser user;





}

package com.sartora.Sartora_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "local_user")
@Getter
@Setter

public class LocalUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="username" ,nullable=false, unique = true)
    private String username;

    @JsonIgnore
    @Column(name="password" ,nullable=false, length = 1000)
    private String password;

    @Column(name="email", nullable=false, unique = true, length = 300)
    private String email;

    @Column(name="first_name", nullable=false)
    private String firstName;

    @Column(name=" last_name", nullable=false)
    private String lastName;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Address> addresses =  new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    private List<VerificationToken> verificationTokens =  new ArrayList<>();

    @Column(name="email_verified", nullable=false)
    private Boolean emailVerified = false;
}

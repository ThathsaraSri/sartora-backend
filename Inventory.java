package com.sartora.Sartora_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory")
@Getter
@Setter

public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="quantity" ,nullable=false)
    private Integer quantity;

    @JsonIgnore
    @OneToOne(optional=false, orphanRemoval=true)
    @JoinColumn(name = "product_id" ,nullable = false, unique = true)
    private Product product;


}

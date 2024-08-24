package com.sartora.Sartora_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "product")
@Getter
@Setter

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="name" ,nullable=false)
    private String name;

    @Column(name="short_description" ,nullable=false)
    private String shortDescription;

    @Column(name="long_description", nullable=false)
    private String longDescription;

    @Column(name="price", nullable=false)
    private double price;

    @OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Inventory inventory;


}

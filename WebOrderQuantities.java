package com.sartora.Sartora_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "web_order_quantities")
@Getter
@Setter


public class WebOrderQuantities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="quantity" ,nullable=false)
    private Integer quantity;


    @ManyToOne(optional=false)
    @JoinColumn(name = "product_id" ,nullable = false)
    private Product product;

    @JsonIgnore
    @ManyToOne(optional=false)
    @JoinColumn(name = "order_id" ,nullable = false)
    private WebOrder order;




}

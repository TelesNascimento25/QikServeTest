package com.qikserve.checkout.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "promotion")
@Data
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String type;

    private int requiredQty;
    private int freeQty;
    private int price;
    private int amount;


}
package com.qikserve.checkout.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "promotion")
@Data
@ToString(exclude = {"promotions"})
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String type;

    private Integer requiredQty;
    private Integer freeQty;
    private Integer price;
    private Integer amount;


}
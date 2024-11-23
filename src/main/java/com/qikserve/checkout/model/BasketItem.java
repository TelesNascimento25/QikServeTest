package com.qikserve.checkout.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@Table(name = "basket_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "basket_id", nullable = false)
    private Long basketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id", insertable = false, updatable = false)
    private Basket basket;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(nullable = false)
    private Integer quantity;
}
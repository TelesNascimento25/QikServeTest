package com.qikserve.checkout.repository;

import com.qikserve.checkout.model.Basket;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    @EntityGraph(attributePaths = {"items.productId", "items.quantity"})
    @Query("from Basket b where b.id = :id")
    Optional<Basket> fetchCheckoutItemsById(Long id);

    @Modifying
    @Query("delete from BasketItem bi where bi.basket.id = :basketId")
    int clearBasket(Long basketId);
}

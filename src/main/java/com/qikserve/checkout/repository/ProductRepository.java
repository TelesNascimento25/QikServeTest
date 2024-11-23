package com.qikserve.checkout.repository;

import com.qikserve.checkout.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository {
    Product findById(String productId);
    List<Product> findAll();
}

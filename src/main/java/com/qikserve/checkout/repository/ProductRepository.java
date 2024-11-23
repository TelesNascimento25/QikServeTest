package com.qikserve.checkout.repository;

import com.qikserve.checkout.model.dto.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository {
    Product findById(String productId);
    List<Product> findAll();
}

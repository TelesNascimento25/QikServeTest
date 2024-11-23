package com.qikserve.checkout.provider;

import org.springframework.http.ResponseEntity;

public interface ProductProvider {
    ResponseEntity<?> getAllProducts();
    ResponseEntity<?> getProductById(String id);
}

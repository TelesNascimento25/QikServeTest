package com.qikserve.checkout.provider;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WiremockProductProvider implements ProductProvider{

    @Value("${wiremock.base.url}")
    private String wiremockBaseUrl;


    @Override
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(wiremockBaseUrl + "/products").
    }

    @Override
    public ResponseEntity<?> getProductById(String id) {
        return null;
    }
}

package com.qikserve.checkout.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final WireMock wireMock;

    public ProductController(Wiremock wireMock){
        this.wireMock = wireMock;
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(){
        return ResponseEntity.ok(wireMock.get("/products").willReturn(wireMock.ok)));
    }
}
@GetMapping("/{id}")
public ResponseEntity<?> getProductById(@PathVariable String id){
    return ResponseEntity.ok(wireMock.get("/products" + id).willReturn(WireMock.ok()));
}

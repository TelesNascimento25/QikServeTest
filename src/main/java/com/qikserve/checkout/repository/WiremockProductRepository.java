package com.qikserve.checkout.repository;

import com.qikserve.checkout.model.dto.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Repository
public class WiremockProductRepository implements ProductRepository {

    private final RestTemplate restTemplate;
    private final String wiremockBaseUrl;

    public WiremockProductRepository(RestTemplate restTemplate, @Value("${wiremock.base.url}") String wiremockBaseUrl) {
        this.restTemplate = restTemplate;
        this.wiremockBaseUrl = wiremockBaseUrl;
    }

    @Override
    public Product findById(String productId) {
        String url = wiremockBaseUrl + "/products/" + productId;
        return restTemplate.getForObject(url, Product.class);
    }

    @Override
    public List<Product> findAll() {
        String url = wiremockBaseUrl + "/products";
        ResponseEntity<Product[]> response = restTemplate.getForEntity(url, Product[].class);
        return Arrays.asList(response.getBody());
    }
}


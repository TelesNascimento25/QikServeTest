package com.qikserve.checkout.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class Product {
    private String id;
    private String name;
    private int price;
    private List<Promotion> promotions;
}
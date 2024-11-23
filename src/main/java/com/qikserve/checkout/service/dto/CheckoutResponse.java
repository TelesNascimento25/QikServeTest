package com.qikserve.checkout.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CheckoutResponse {
    private BigDecimal total;
    private BigDecimal totalSavings;
    private String error;
}
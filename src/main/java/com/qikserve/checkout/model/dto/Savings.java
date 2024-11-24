package com.qikserve.checkout.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class Savings {
    private BigDecimal totalPrice;
    private BigDecimal promotionalPrice;
    private BigDecimal finalPrice;
}
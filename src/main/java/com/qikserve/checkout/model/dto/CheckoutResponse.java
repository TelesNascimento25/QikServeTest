package com.qikserve.checkout.model.dto;

import com.qikserve.checkout.model.CheckoutMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CheckoutResponse {
    private BigDecimal total;
    private BigDecimal totalSavings;
    private CheckoutMessages messages;
}
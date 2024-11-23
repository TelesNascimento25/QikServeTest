package com.qikserve.checkout.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CheckoutMessages {
    private String totalMessage;
    private String totalSavingsMessage;
    private String errorMessage;
}

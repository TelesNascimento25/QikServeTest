package com.qikserve.checkout.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutMessages {
    private String totalMessage;
    private String totalSavingsMessage;
}

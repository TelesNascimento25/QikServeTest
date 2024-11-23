package com.qikserve.checkout.service.promotion;

import com.qikserve.checkout.model.Product;

import java.math.BigDecimal;

public interface PromotionStrategy {
    BigDecimal applyPromotion(Product product, int quantity);
}

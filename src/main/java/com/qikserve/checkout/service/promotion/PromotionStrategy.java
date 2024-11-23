package com.qikserve.checkout.service.promotion;

import com.qikserve.checkout.model.dto.Product;
import com.qikserve.checkout.model.dto.PromotionType;

import java.math.BigDecimal;

public interface PromotionStrategy {
    BigDecimal applyPromotion(Product product, int quantity);
    PromotionType getPromotionType();
}

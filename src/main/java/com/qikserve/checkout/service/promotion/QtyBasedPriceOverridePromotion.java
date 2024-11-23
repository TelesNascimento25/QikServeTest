package com.qikserve.checkout.service.promotion;

import com.qikserve.checkout.model.Product;
import com.qikserve.checkout.model.Promotion;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class QtyBasedPriceOverridePromotion implements PromotionStrategy {

    @Override
    public BigDecimal applyPromotion(Product product, int quantity) {
        Promotion promotion = product.getPromotions().stream()
                .filter(p -> "QTY_BASED_PRICE_OVERRIDE".equals(p.getType()))
                .findFirst()
                .orElse(null);

        if (promotion != null && quantity >= promotion.getRequiredQty()) {
            BigDecimal originalPrice = BigDecimal.valueOf(product.getPrice()).divide(BigDecimal.valueOf(100));
            BigDecimal promotionalPrice = BigDecimal.valueOf(promotion.getPrice()).divide(BigDecimal.valueOf(100));
            int sets = quantity / promotion.getRequiredQty();
            int remaining = quantity % promotion.getRequiredQty();

            return originalPrice.multiply(BigDecimal.valueOf(quantity)).subtract(promotionalPrice.multiply(BigDecimal.valueOf((long) sets * promotion.getRequiredQty()))).add(originalPrice.multiply(BigDecimal.valueOf(remaining)));

        }
        return BigDecimal.ZERO;
    }
}
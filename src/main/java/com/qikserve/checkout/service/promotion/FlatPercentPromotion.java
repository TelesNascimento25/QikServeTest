package com.qikserve.checkout.service.promotion;

import com.qikserve.checkout.model.Product;
import com.qikserve.checkout.model.Promotion;
import com.qikserve.checkout.model.PromotionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FlatPercentPromotion implements PromotionStrategy {

    @Override
    public BigDecimal applyPromotion(Product product, int quantity) {
        Promotion promotion = product.getPromotions().stream()
                .filter(p -> PromotionType.FLAT_PERCENT.equals(p.getType()))
                .findFirst()
                .orElse(null);

        if (promotion != null) {
            BigDecimal originalPrice = BigDecimal.valueOf(product.getPrice()).divide(BigDecimal.valueOf(100));
            BigDecimal totalPrice = originalPrice.multiply(BigDecimal.valueOf(quantity));
            return totalPrice.multiply(BigDecimal.valueOf(promotion.getAmount()).divide(BigDecimal.valueOf(100)));
        }

        return BigDecimal.ZERO;
    }

    @Override
    public PromotionType getPromotionType() {
        return PromotionType.FLAT_PERCENT;
    }
}

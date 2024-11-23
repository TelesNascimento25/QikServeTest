package com.qikserve.checkout.service.promotion;

import com.qikserve.checkout.model.dto.Product;
import com.qikserve.checkout.model.dto.Promotion;
import com.qikserve.checkout.model.dto.PromotionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BuyXGetYFreePromotion implements PromotionStrategy {

    @Override
    public PromotionType getPromotionType() {
        return PromotionType.BUY_X_GET_Y_FREE;
    }

    @Override
    public BigDecimal applyPromotion(Product product, int quantity) {
        Promotion promotion = product.getPromotions()
                .stream()
                .filter(p -> PromotionType.BUY_X_GET_Y_FREE.equals(p.getType()))
                .findFirst()
                .orElse(null);

        if (promotion != null) {
            BigDecimal originalPrice = BigDecimal.valueOf(product.getPrice())
                    .divide(BigDecimal.valueOf(100));
            int sets = quantity / promotion.getRequiredQty();
            int freeItems = sets * promotion.getFreeQty();

            return originalPrice.multiply(BigDecimal.valueOf(freeItems));
        }
        return BigDecimal.ZERO;
    }

}

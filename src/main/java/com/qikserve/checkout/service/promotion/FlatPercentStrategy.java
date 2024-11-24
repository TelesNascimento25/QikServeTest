package com.qikserve.checkout.service.promotion;

import com.qikserve.checkout.model.dto.FlatPercent;
import com.qikserve.checkout.model.dto.Promotion.PromotionType;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
public class FlatPercentStrategy implements PromotionStrategy {
    private final FlatPercent promotion;

    @Override
    public BigDecimal computeFinalPriceInPence(int quantity, int priceInPence) {
        var percent = BigDecimal.valueOf(promotion.getAmount()).divide(BigDecimal.valueOf(100), RoundingMode.UNNECESSARY);
        var factor = BigDecimal.valueOf(quantity).multiply(percent);
        return BigDecimal.valueOf(priceInPence).multiply(factor);
    }

    @Override
    public boolean isApplicable(int quantity, int priceInPence) {
        return true;
    }

    @Override
    public PromotionType getPromotionType() {
        return PromotionType.FLAT_PERCENT;
    }

}

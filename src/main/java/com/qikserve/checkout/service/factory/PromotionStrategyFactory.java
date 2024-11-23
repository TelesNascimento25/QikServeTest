package com.qikserve.checkout.service.factory;

import com.qikserve.checkout.service.promotion.BuyXGetYFreePromotion;
import com.qikserve.checkout.service.promotion.FlatPercentPromotion;
import com.qikserve.checkout.service.promotion.PromotionStrategy;
import com.qikserve.checkout.service.promotion.QtyBasedPriceOverridePromotion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PromotionStrategyFactory {

    private final Map<String, PromotionStrategy> strategies;

    public PromotionStrategyFactory(List<PromotionStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(strategy -> {
                    if (strategy instanceof BuyXGetYFreePromotion) return "BUY_X_GET_Y_FREE";
                    if (strategy instanceof QtyBasedPriceOverridePromotion) return "QTY_BASED_PRICE_OVERRIDE";
                    if (strategy instanceof FlatPercentPromotion) return "FLAT_PERCENT";
                    return null;
                }, strategy -> strategy));
    }

    public PromotionStrategy getStrategy(String promotionType) {
        return strategies.get(promotionType);
    }
}
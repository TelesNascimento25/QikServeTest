package com.qikserve.checkout.service.factory;

import com.qikserve.checkout.model.dto.PromotionType;
import com.qikserve.checkout.service.promotion.PromotionStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PromotionStrategyFactory {

    private final Map<PromotionType, PromotionStrategy> strategies;

    public PromotionStrategyFactory(List<PromotionStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(PromotionStrategy::getPromotionType, s -> s));
    }

    public PromotionStrategy getStrategy(PromotionType promotionType) {
        return strategies.get(promotionType);
    }
}

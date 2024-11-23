package com.qikserve.checkout.service;

import com.qikserve.checkout.model.Basket;
import com.qikserve.checkout.model.BasketItem;
import com.qikserve.checkout.model.Product;
import com.qikserve.checkout.repository.ProductRepository;
import com.qikserve.checkout.service.dto.CheckoutResponse;
import com.qikserve.checkout.service.promotion.PromotionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private List<PromotionStrategy> promotionStrategies;

    @Override
    public CheckoutResponse calculateTotal(Basket basket) {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalSavings = BigDecimal.ZERO;

        Map<String, Integer> productQuantities = basket.getItems().stream()
                .collect(Collectors.toMap(BasketItem::getProductId, BasketItem::getQuantity, Integer::sum));

        for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
            String productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

            BigDecimal finalPrice = BigDecimal.valueOf(product.getPrice()).divide(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(quantity));

            for (PromotionStrategy strategy : promotionStrategies) {
                BigDecimal discount = strategy.applyPromotion(product, quantity);
                finalPrice = finalPrice.subtract(discount);
                totalSavings = totalSavings.add(discount);
            }
            total = total.add(finalPrice);
        }

        return new CheckoutResponse(total, totalSavings, null);
    }
}
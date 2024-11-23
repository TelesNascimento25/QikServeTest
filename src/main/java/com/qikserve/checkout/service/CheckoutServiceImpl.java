package com.qikserve.checkout.service;

import com.qikserve.checkout.model.Basket;
import com.qikserve.checkout.model.BasketItem;
import com.qikserve.checkout.model.Product;
import com.qikserve.checkout.model.Promotion;
import com.qikserve.checkout.repository.ProductRepository;
import com.qikserve.checkout.service.dto.CheckoutResponse;
import com.qikserve.checkout.service.factory.PromotionStrategyFactory;
import com.qikserve.checkout.service.promotion.PromotionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PromotionStrategyFactory promotionStrategyFactory;

    @Override
    @Transactional(readOnly = true)
    public CheckoutResponse calculateTotal(Basket basket) {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (BasketItem basketItem : basket.getItems()) {
            String productId = basketItem.getProductId();
            int quantity = basketItem.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

            BigDecimal originalPrice = BigDecimal.valueOf(product.getPrice())
                    .divide(BigDecimal.valueOf(100))
                    .multiply(BigDecimal.valueOf(quantity));

            BigDecimal finalPrice = originalPrice;

            if (!product.getPromotions().isEmpty()) {
                for (Promotion promotion : product.getPromotions()) {
                    PromotionStrategy strategy = promotionStrategyFactory.getStrategy(promotion.getType());
                    if (strategy != null) {
                        BigDecimal discount = strategy.applyPromotion(product, quantity);
                        finalPrice = finalPrice.subtract(discount);
                        totalDiscount = totalDiscount.add(discount);
                    }
                }
            }
            total = total.add(finalPrice);

        }
        return new CheckoutResponse(total, totalDiscount, null);
    }
}
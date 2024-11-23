package com.qikserve.checkout.service;

import com.qikserve.checkout.model.*;
import com.qikserve.checkout.repository.ProductRepository;
import com.qikserve.checkout.service.dto.CheckoutResponse;
import com.qikserve.checkout.service.factory.PromotionStrategyFactory;
import com.qikserve.checkout.service.promotion.PromotionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PromotionStrategyFactory promotionStrategyFactory;
    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional(readOnly = true)
    public CheckoutResponse calculateTotal(Basket basket) {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (BasketItem basketItem : basket.getItems()) {
            String productId = basketItem.getProductId();
            int quantity = basketItem.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("error.productNotFound",
                            new Object[]{productId},
                            LocaleContextHolder.getLocale())));

            BigDecimal finalPrice = BigDecimal.valueOf(product.getPrice())
                    .divide(BigDecimal.valueOf(100))
                    .multiply(BigDecimal.valueOf(quantity));

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

        CheckoutMessages messages = createMessages(total, totalDiscount);

        return CheckoutResponse.builder()
                .total(total)
                .totalSavings(totalDiscount)
                .messages(messages)
                .build();
    }

    private CheckoutMessages createMessages(BigDecimal total, BigDecimal totalSavings) {
        String totalMessage = messageSource.getMessage("checkout.total", new Object[]{total}, LocaleContextHolder.getLocale());
        String savingsMessage = messageSource.getMessage("checkout.savings", new Object[]{totalSavings}, LocaleContextHolder.getLocale());
        return CheckoutMessages.builder()
                .totalMessage(totalMessage)
                .totalSavingsMessage(savingsMessage)
                .build();
    }
}
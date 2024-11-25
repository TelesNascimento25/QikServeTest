package com.qikserve.checkout.service;

import com.github.tomakehurst.wiremock.admin.NotFoundException;
import com.qikserve.checkout.exception.BasketNotFoundException;
import com.qikserve.checkout.exception.BasketNotOpenException;
import com.qikserve.checkout.model.Basket;
import com.qikserve.checkout.model.BasketItem;
import com.qikserve.checkout.model.BasketStatus;
import com.qikserve.checkout.model.dto.Savings;
import com.qikserve.checkout.repository.BasketItemRepository;
import com.qikserve.checkout.repository.BasketRepository;
import com.qikserve.checkout.util.PenceUtils;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final BasketItemService basketItemService;
    private final BasketItemRepository basketItemRepository;
    private final MessageSource messageSource;

    @Observed(name = "basket.get")
    public Optional<Basket> getBasket(Long basketId) {
        return basketRepository.findById(basketId);
    }

    @Observed(name = "basket.create")
    public Basket createBasket() {
        var basket = Basket.builder()
                .status(BasketStatus.OPEN)
                .build();
        return basketRepository.save(basket);
    }
    @Observed(name = "basket.addItem")
    public BasketItem addBasketItem(Long basketId, @Valid BasketItem basketItem) {
        if (basketItem.getQuantity() <= 0) {
            String errorMessage = messageSource.
                    getMessage("error.quantityGreaterThanZero", null, LocaleContextHolder.getLocale());
            throw new IllegalArgumentException(errorMessage);
        }
        var basketStatus = this.getBasketById(basketId, Basket::getStatus);

        if (!BasketStatus.OPEN.equals(basketStatus)) {
            throw new BasketNotOpenException(basketId);
        }

        return basketItemRepository.save(BasketItem.builder()
                .basketId(basketItem.getBasketId())
                .productId(basketItem.getProductId())
                .quantity(basketItem.getQuantity())
                .build());

    }
    @Observed(name = "basket.cancel")
    public void cancelBasket(Long basketId) {
        this.updateById(basketId, basket -> basket.setStatus(BasketStatus.CANCELLED));
    }
    @Observed(name = "basket.clear")
    public void clearBasket(Long basketId) {
        basketRepository.clearBasket(basketId);
    }
    @Observed(name = "basket.calculateSavings")
    public Savings calculateSavings(Long basketId) {
        return getBasketById(basketId, basket -> {
            var basketItems = basket.getBasketItems();
            BigDecimal totalPrice = PenceUtils.toPounds(basketItemService.computeTotalPrice(basketItems));
            BigDecimal promotionalPrice = PenceUtils.toPounds(basketItemService.computePromotionalPrice(basketItems));
            BigDecimal finalPrice = totalPrice.subtract(promotionalPrice);

            return Savings.builder()
                    .totalPrice(totalPrice)
                    .promotionalPrice(promotionalPrice)
                    .finalPrice(finalPrice)
                    .build();
        });
    }
    @Observed(name = "basket.checkout")
    public Basket checkout(Long basketId) {
        this.updateById(basketId, basket -> basket.setStatus(BasketStatus.CHECKED_OUT));

        var basket = basketRepository.fetchCheckoutItemsById(basketId).orElseThrow(RuntimeException::new);
        if (!BasketStatus.OPEN.equals(basket.getStatus())) {
            throw new BasketNotOpenException(basketId);
        }

        final var basketItems = basket.getBasketItems();
        BigDecimal total = PenceUtils.toPounds(basketItemService.computePromotionalPrice(basketItems));

        basket.setStatus(BasketStatus.CHECKED_OUT);
        basket.setTotal(total);
        return basketRepository.save(basket);
    }

    @Observed(name = "basket.getById")
    private <T> T getBasketById(Long id, Function<Basket, T> transformer) {
        return basketRepository.findById(id)
                .map(transformer)
                .orElseThrow(() -> new BasketNotFoundException(id));
    }
    @Observed(name = "basket.update")
    private Basket updateById(Long basketId, Consumer<Basket> consumer) {
        return this.getBasketById(basketId, basket -> {
            consumer.accept(basket);
            return basketRepository.save(basket);
        });
    }

}

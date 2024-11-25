package com.qikserve.checkout.service;

import com.github.tomakehurst.wiremock.admin.NotFoundException;
import com.qikserve.checkout.exception.BasketItemNotFoundException;
import com.qikserve.checkout.model.BasketItem;
import com.qikserve.checkout.model.dto.Product;
import com.qikserve.checkout.repository.BasketItemRepository;
import com.qikserve.checkout.repository.ProductRepository;
import com.qikserve.checkout.service.factory.PromotionStrategyFactory;
import com.qikserve.checkout.util.PenceUtils;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasketItemService {
    private final ProductRepository productRepository;

    private final BasketItemRepository basketItemRepository;

    private final MessageSource messageSource;

    @Observed(name = "basketItem.computePromotionalPrice")
    @Cacheable("promotionalPrice")
    public BigDecimal computePromotionalPrice(Collection<BasketItem> items) {
        final var productsById = this.getProductsById(items);
        return items.stream()
            .map(item -> PromotionStrategyFactory
                    .applyPromotions(productsById.get(item.getProductId()), item.getQuantity()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    @Observed(name = "basketItem.computeTotalPrice")
    @Cacheable("totalPrice")
    public BigDecimal computeTotalPrice(Collection<BasketItem> items) {
        final var productsById = this.getProductsById(items);
        return items.stream()
            .map(item -> PenceUtils.computeTotal(item.getQuantity(), productsById.get(item.getProductId()).getPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    @Observed(name = "basketItem.computeTotalSavings")
    @Cacheable("totalSavings")
    public BigDecimal computeTotalSavings(Collection<BasketItem> items) {
        return computeTotalPrice(items).subtract(computePromotionalPrice(items));
    }
    @Observed(name = "basketItem.getProductsById")
    @Cacheable("products")
    public Map<String, Product> getProductsById(Collection<BasketItem> items) {
        return productRepository.findAllById(items.stream()
                .map(BasketItem::getProductId)
                .collect(Collectors.toSet())).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));
    }
    @Observed(name = "basketItem.get")
    public Optional<BasketItem> getBasketItem(Long basketItemId) {
        return basketItemRepository.findById(basketItemId);
    }
    @Observed(name = "basketItem.updateQuantity")
    public BasketItem updateQuantityBasketItem(Long id, int quantity) {
        return this.updateById(id, item -> item.setQuantity(quantity));
    }
    @Observed(name = "basketItem.delete")
    public void deleteBasketItem(Long basketItemId) {
        this.getById(basketItemId);
        basketItemRepository.deleteById(basketItemId);
    }

    private BasketItem getById(Long id) {
        return this.getById(id, Function.identity());
    }
    @Observed(name = "basketItem.getById")
    private <T> T getById(Long id, Function<BasketItem, T> transformer) {
        return basketItemRepository.findById(id)
                .map(transformer)
                .orElseThrow(() -> new BasketItemNotFoundException(id));
    }
    @Observed(name = "basketItem.update")
    private BasketItem updateById(Long id, Consumer<BasketItem> consumer) {
        return this.getById(id, item -> {
            consumer.accept(item);
            return basketItemRepository.save(item);
        });
    }

}

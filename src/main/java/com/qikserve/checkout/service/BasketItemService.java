package com.qikserve.checkout.service;

import com.github.tomakehurst.wiremock.admin.NotFoundException;
import com.qikserve.checkout.model.BasketItem;
import com.qikserve.checkout.model.dto.Product;
import com.qikserve.checkout.repository.BasketItemRepository;
import com.qikserve.checkout.repository.ProductRepository;
import com.qikserve.checkout.service.factory.PromotionStrategyFactory;
import com.qikserve.checkout.util.PenceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable("promotionalPrice")
    public BigDecimal computePromotionalPrice(Collection<BasketItem> items) {
        final var productsById = this.getProductsById(items);
        return items.stream()
            .map(item -> PromotionStrategyFactory
                    .applyPromotions(productsById.get(item.getProductId()), item.getQuantity()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Cacheable("totalPrice")
    public BigDecimal computeTotalPrice(Collection<BasketItem> items) {
        final var productsById = this.getProductsById(items);
        return items.stream()
            .map(item -> PenceUtils.computeTotal(item.getQuantity(), productsById.get(item.getProductId()).getPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Cacheable("totalSavings")
    public BigDecimal computeTotalSavings(Collection<BasketItem> items) {
        return computeTotalPrice(items).subtract(computePromotionalPrice(items));
    }

    @Cacheable("products")
    public Map<String, Product> getProductsById(Collection<BasketItem> items) {
        return productRepository.findAllById(items.stream()
                .map(BasketItem::getProductId)
                .collect(Collectors.toSet())).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));
    }

    public Optional<BasketItem> getBasketItem(Long basketItemId) {
        return basketItemRepository.findById(basketItemId);
    }

    public BasketItem updateQuantityBasketItem(Long id, int quantity) {
        return this.updateById(id, item -> item.setQuantity(quantity));
    }

    public void deleteBasketItem(Long basketItemId) {
        this.getById(basketItemId);
        basketItemRepository.deleteById(basketItemId);
    }

    private BasketItem getById(Long id) {
        return this.getById(id, Function.identity());
    }

    private <T> T getById(Long id, Function<BasketItem, T> transformer) {
        return basketItemRepository.findById(id)
                .map(transformer)
                .orElseThrow(() -> new NotFoundException("Basket item with id: " + id + " not found"));
    }

    private BasketItem updateById(Long id, Consumer<BasketItem> consumer) {
        return this.getById(id, item -> {
            consumer.accept(item);
            return basketItemRepository.save(item);
        });
    }

}

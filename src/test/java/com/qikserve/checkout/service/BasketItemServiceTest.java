package com.qikserve.checkout.service;

import com.qikserve.checkout.exception.BasketItemNotFoundException;
import com.qikserve.checkout.model.BasketItem;
import com.qikserve.checkout.model.dto.Product;
import com.qikserve.checkout.repository.BasketItemRepository;
import com.qikserve.checkout.repository.ProductRepository;
import com.qikserve.checkout.service.factory.PromotionStrategyFactory;
import com.qikserve.checkout.util.PenceUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BasketItemServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BasketItemRepository basketItemRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private BasketItemService basketItemService;

//    @Test
//    public void computePromotionalPrice_WhenItemsExist_ThenReturnPromotionalPrice() {
//        // Given
//        var items = List.of(
//                BasketItem.builder().productId("A").quantity(2).build(),
//                BasketItem.builder().productId("B").quantity(1).build()
//        );
//        var productA = Product.builder().id("A").price(BigDecimal.valueOf(10)).build();
//        var productB = Product.builder().id("B").price(BigDecimal.valueOf(20)).build();
//        when(productRepository.findAllById(any())).thenReturn(List.of(productA, productB));
//
//
//        // When
//        BigDecimal promotionalPrice = basketItemService.computePromotionalPrice(items);
//
//        // Then
//        BigDecimal expectedPrice = PromotionStrategyFactory.applyPromotions(productA, 2)
//                                                           .add(PromotionStrategyFactory.applyPromotions(productB, 1));
//        assertEquals(expectedPrice, promotionalPrice);
//    }
//
//    @Test
//    public void computeTotalPrice_WhenItemsExist_ThenReturnTotalPrice() {
//        // Given
//        var items = List.of(
//                BasketItem.builder().productId("A").quantity(2).build(),
//                BasketItem.builder().productId("B").quantity(1).build()
//        );
//        var productA = Product.builder().id("A").price(BigDecimal.valueOf(10)).build();
//        var productB = Product.builder().id("B").price(BigDecimal.valueOf(20)).build();
//        when(productRepository.findAllById(any())).thenReturn(List.of(productA, productB));
//
//        // When
//        BigDecimal totalPrice = basketItemService.computeTotalPrice(items);
//
//        // Then
//        BigDecimal expectedPrice = PenceUtils.computeTotal(2, productA.getPrice())
//                                             .add(PenceUtils.computeTotal(1, productB.getPrice()));
//
//        assertEquals(expectedPrice, totalPrice);
//    }

//    @Test
//    public void computeTotalSavings_WhenItemsExist_ThenReturnTotalSavings() {
//        // Given
//        var items = List.of(
//                BasketItem.builder().productId("A").quantity(2).build(),
//                BasketItem.builder().productId("B").quantity(1).build()
//        );
//        var productA = Product.builder().id("A").price(BigDecimal.valueOf(10)).build();
//        var productB = Product.builder().id("B").price(BigDecimal.valueOf(20)).build();
//
//        when(productRepository.findAllById(any())).thenReturn(List.of(productA, productB));
//
//        BigDecimal totalPrice = PenceUtils.computeTotal(2, productA.getPrice())
//                                          .add(PenceUtils.computeTotal(1, productB.getPrice()));
//
//        BigDecimal promotionalPrice = PromotionStrategyFactory.applyPromotions(productA, 2)
//                                                              .add(PromotionStrategyFactory.applyPromotions(productB, 1));
//
//
//        // When
//        BigDecimal totalSavings = basketItemService.computeTotalSavings(items);
//
//        // Then
//        assertEquals(totalPrice.subtract(promotionalPrice), totalSavings);
//    }

    @Test
    public void getProductsById_WhenItemsHaveProducts_ThenReturnProducts() {
        // Given
        var items = List.of(
                BasketItem.builder().productId("A").build(),
                BasketItem.builder().productId("B").build()
        );
        var productA = Product.builder().id("A").build();
        var productB = Product.builder().id("B").build();
        when(productRepository.findAllById(any())).thenReturn(List.of(productA, productB));

        // When
        Map<String, Product> productsById = basketItemService.getProductsById(items);

        // Then
        assertEquals(2, productsById.size());
        assertTrue(productsById.containsKey("A"));
        assertTrue(productsById.containsKey("B"));
    }

    @Test
    public void getBasketItem_WhenBasketItemExists_ThenReturnBasketItem() {
        // Given
        var basketItemId = 1L;
        var basketItem = BasketItem.builder().id(basketItemId).build();
        when(basketItemRepository.findById(basketItemId)).thenReturn(Optional.of(basketItem));

        // When
        Optional<BasketItem> result = basketItemService.getBasketItem(basketItemId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(basketItem, result.get());
    }


    @Test
    public void getBasketItem_WhenBasketItemDoesNotExist_ThenReturnEmpty() {
        // Given
        var basketItemId = 1L;
        when(basketItemRepository.findById(basketItemId)).thenReturn(Optional.empty());

        // When
        Optional<BasketItem> result = basketItemService.getBasketItem(basketItemId);

        // Then
        assertTrue(result.isEmpty());
    }


    @Test
    void updateQuantityBasketItem_WhenBasketItemExists_ThenReturnUpdatedBasketItem() {
        // Given
        Long basketItemId = 1L;
        int newQuantity = 5;
        BasketItem existingBasketItem = BasketItem.builder().id(basketItemId).quantity(2).build();
        BasketItem updatedBasketItem = BasketItem.builder().id(basketItemId).quantity(newQuantity).build();
        when(basketItemRepository.findById(basketItemId)).thenReturn(Optional.of(existingBasketItem));
        when(basketItemRepository.save(any(BasketItem.class))).thenAnswer(invocation -> invocation.getArgument(0));


        // When
        BasketItem result = basketItemService.updateQuantityBasketItem(basketItemId, newQuantity);


        // Then
        assertEquals(newQuantity, result.getQuantity());

    }

    @Test
    void deleteBasketItem_WhenBasketItemExists_ThenDeleteBasketItem() {
        // Given
        Long basketItemId = 1L;
        BasketItem existingBasketItem = BasketItem.builder().id(basketItemId).build();
        when(basketItemRepository.findById(basketItemId)).thenReturn(Optional.of(existingBasketItem));

        // When
        basketItemService.deleteBasketItem(basketItemId);

        // Then
        verify(basketItemRepository, times(1)).deleteById(basketItemId);
    }


    @Test
    void deleteBasketItem_WhenBasketItemDoesNotExist_ThenThrowException() {

        // Given
        Long basketItemId = 1L;
        when(basketItemRepository.findById(basketItemId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BasketItemNotFoundException.class, () -> basketItemService.deleteBasketItem(basketItemId));
    }

}

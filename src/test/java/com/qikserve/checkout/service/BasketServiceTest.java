package com.qikserve.checkout.service;

import com.qikserve.checkout.exception.BasketNotOpenException;
import com.qikserve.checkout.model.Basket;
import com.qikserve.checkout.model.BasketItem;
import com.qikserve.checkout.model.BasketStatus;
import com.qikserve.checkout.model.dto.Savings;
import com.qikserve.checkout.repository.BasketItemRepository;
import com.qikserve.checkout.repository.BasketRepository;
import com.qikserve.checkout.service.BasketItemService;
import com.qikserve.checkout.service.BasketService;
import com.qikserve.checkout.util.PenceUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private BasketItemService basketItemService;

    @Mock
    private BasketItemRepository basketItemRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private BasketService basketService;

    @Test
    public void getBasket_WhenBasketExists_ThenReturnBasket() {
        // Given
        var basketId = 1L;
        var basket = Basket.builder().id(basketId).status(BasketStatus.OPEN).build();
        when(basketRepository.findById(basketId)).thenReturn(Optional.of(basket));

        // When
        Optional<Basket> result = basketService.getBasket(basketId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(basket, result.get());
    }

    @Test
    public void getBasket_WhenBasketDoesNotExist_ThenReturnEmpty() {
        // Given
        var basketId = 1L;
        when(basketRepository.findById(basketId)).thenReturn(Optional.empty());

        // When
        Optional<Basket> result = basketService.getBasket(basketId);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void createBasket_ThenReturnNewBasket() {
        // Given
        var basket = Basket.builder().status(BasketStatus.OPEN).build();
        when(basketRepository.save(any(Basket.class))).thenAnswer(invocation -> {
            Basket savedBasket = invocation.getArgument(0);
            savedBasket.setId(1L);
            return savedBasket;
        });

        // When
        Basket result = basketService.createBasket();

        // Then
        assertNotNull(result);
        assertEquals(BasketStatus.OPEN, result.getStatus());
        assertEquals(1L, result.getId());
    }


    @Test
    public void addBasketItem_WhenBasketIsOpen_ThenSaveBasketItem() {
        // Given
        Long basketId = 1L;
        BasketItem basketItem = BasketItem.builder().basketId(basketId).productId("1").quantity(1).build();
        Basket basket = Basket.builder().id(basketId).status(BasketStatus.OPEN).build();

        when(basketRepository.findById(basketId)).thenReturn(Optional.of(basket));
        when(basketItemRepository.save(any(BasketItem.class))).thenReturn(basketItem);

        // When
        BasketItem result = basketService.addBasketItem(basketId, basketItem);

        // Then
        assertEquals(basketItem, result);
    }

    @Test
    public void addBasketItem_WhenBasketIsNotOpen_ThenThrowException() {
        // Given
        Long basketId = 1L;
        BasketItem basketItem = BasketItem.builder().basketId(basketId).productId("1").quantity(1).build();
        Basket basket = Basket.builder().id(basketId).status(BasketStatus.CANCELLED).build();

        when(basketRepository.findById(basketId)).thenReturn(Optional.of(basket));

        // When & Then
        assertThrows(BasketNotOpenException.class, () -> basketService.addBasketItem(basketId, basketItem));
    }

    @Test
    public void cancelBasket_WhenBasketExists_ThenUpdateStatus() {
        // Given
        var basketId = 1L;
        var basket = Basket.builder().id(basketId).status(BasketStatus.OPEN).build();
        when(basketRepository.findById(basketId)).thenReturn(Optional.of(basket));
        when(basketRepository.save(any())).thenReturn(basket);

        // When
        basketService.cancelBasket(basketId);

        // Then
        assertEquals(BasketStatus.CANCELLED, basket.getStatus());
        verify(basketRepository, times(1)).save(basket);
    }


    @Test
    public void clearBasket_WhenBasketExists_ThenClearBasketItems() {
        // Given
        var basketId = 1L;

        // When
        basketService.clearBasket(basketId);

        // Then
        verify(basketRepository, times(1)).clearBasket(basketId);
    }

    @Test
    public void calculateSavings_WhenBasketExists_ThenReturnSavings() {
        // Given
        var basketId = 1L;
        var basketItems = List.of(
                BasketItem.builder().productId("A").quantity(2).build(),
                BasketItem.builder().productId("B").quantity(1).build()
        );
        var basket = Basket.builder().id(basketId).basketItems(basketItems).build();


        when(basketRepository.findById(basketId)).thenReturn(Optional.of(basket));

        BigDecimal totalPrice = BigDecimal.valueOf(100);
        BigDecimal promotionalPrice = BigDecimal.valueOf(90);


        when(basketItemService.computeTotalPrice(basketItems)).thenReturn(totalPrice);
        when(basketItemService.computePromotionalPrice(basketItems)).thenReturn(promotionalPrice);


        // When
        Savings savings = basketService.calculateSavings(basketId);

        // Then
        assertEquals(PenceUtils.toPounds(totalPrice), savings.getTotalPrice());
        assertEquals(PenceUtils.toPounds(promotionalPrice), savings.getPromotionalPrice());
        assertEquals(PenceUtils.toPounds(totalPrice.subtract(promotionalPrice)), savings.getFinalPrice());

    }

    @Test
    public void checkout_WhenBasketExists_ThenCheckoutBasket() {

        // Given
        long basketId = 1L;
        BigDecimal total = BigDecimal.TEN;

        List<BasketItem> basketItems = new ArrayList<>();
        Basket basket = Basket.builder()
                              .id(basketId)
                              .status(BasketStatus.OPEN)
                              .basketItems(basketItems)
                              .build();

        when(basketRepository.fetchCheckoutItemsById(basketId)).thenReturn(Optional.of(basket));
        when(basketItemService.computePromotionalPrice(basketItems)).thenReturn(PenceUtils.toPence(total));
        when(basketRepository.save(any(Basket.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Basket result = basketService.checkout(basketId);

        // Then
        assertEquals(BasketStatus.CHECKED_OUT, result.getStatus());
        assertEquals(total, result.getTotal());

    }

    @Test
    void checkout_WhenBasketNotOpen_ThenThrowBasketNotOpenException() {
        // Given
        Long basketId = 1L;
        Basket basket = Basket.builder().id(basketId).status(BasketStatus.CANCELLED).build();
        when(basketRepository.fetchCheckoutItemsById(basketId)).thenReturn(Optional.of(basket));

        // When & Then
        assertThrows(BasketNotOpenException.class, () -> basketService.checkout(basketId));
    }

}


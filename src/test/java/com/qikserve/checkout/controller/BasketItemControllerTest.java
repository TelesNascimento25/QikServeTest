package com.qikserve.checkout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qikserve.checkout.model.BasketItem;
import com.qikserve.checkout.service.BasketItemService;
import com.qikserve.checkout.service.BasketService;
import io.vavr.CheckedFunction1;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.function.Function;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BasketItemController.class)
public class BasketItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BasketItemService basketItemService;

    @MockitoBean
    private BasketService basketService;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Function<Object, String> toStr = CheckedFunction1.of(mapper::writeValueAsString).unchecked();


    @Test
    public void getBasketItem_WhenBasketItemExists_ThenReturnBasketItem() throws Exception {
        // Given
        var basketItemId = 1L;
        var basketItem = BasketItem.builder().id(basketItemId).basketId(1L).productId("product1").quantity(2).build();
        when(basketItemService.getBasketItem(basketItemId)).thenReturn(Optional.of(basketItem));

        // When & Then
        mockMvc.perform(get("/basketItems/{basketItemId}", basketItemId))
               .andExpect(status().isOk())
               .andExpect(content().json(toStr.apply(basketItem)));
    }

    @Test
    public void getBasketItem_WhenBasketItemDoesNotExist_ThenReturnNotFound() throws Exception {
        // Given
        var basketItemId = 1L;
        when(basketItemService.getBasketItem(basketItemId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/basketItems/{basketItemId}", basketItemId))
               .andExpect(status().isNotFound());
    }


    @Test
    public void updateQuantityBasketItem_WhenBasketItemExists_ThenReturnUpdatedBasketItem() throws Exception {
        // Given
        var basketItemId = 1L;
        var basketItem = BasketItem.builder().id(basketItemId).basketId(1L).productId("product1").quantity(2).build();
        when(basketItemService.updateQuantityBasketItem(basketItemId, 5)).thenReturn(basketItem);

        // When & Then
        mockMvc.perform(patch("/basketItems/{basketItemId}", basketItemId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{\"quantity\": 5}"))
               .andExpect(status().isOk())
               .andExpect(content().json(toStr.apply(basketItem)));
    }

    @Test
    public void deleteBasketItem_WhenBasketItemExists_ThenReturnNoContent() throws Exception {
        // Given
        var basketItemId = 1L;

        // When & Then
        mockMvc.perform(delete("/basketItems/{basketItemId}", basketItemId))
               .andExpect(status().isNoContent());
    }


    @Test
    public void createBasketItem_WhenValidRequest_ThenReturnCreated() throws Exception {
        // Given
        var basketItemRequest = BasketItem.builder().basketId(1L).productId("product1").quantity(2).build();
        var basketItemResponse = BasketItem.builder().id(1L).basketId(1L).productId("product1").quantity(2).build();
        when(basketService.addBasketItem(1L, basketItemRequest)).thenReturn(basketItemResponse);

        // When & Then
        mockMvc.perform(post("/basketItems")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(toStr.apply(basketItemRequest)))
               .andExpect(status().isCreated())
               .andExpect(header().string("Location", Matchers.endsWith("/basketItems/1")))
               .andExpect(content().json(toStr.apply(basketItemResponse)));
    }


}

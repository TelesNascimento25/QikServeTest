package com.qikserve.checkout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qikserve.checkout.model.Basket;
import com.qikserve.checkout.model.BasketStatus;
import com.qikserve.checkout.model.dto.BuyXGetYFree;
import com.qikserve.checkout.model.dto.FlatPercent;
import com.qikserve.checkout.model.dto.Product;
import com.qikserve.checkout.model.dto.QtyBasedPriceOverride;
import com.qikserve.checkout.repository.BasketItemRepository;
import com.qikserve.checkout.repository.BasketRepository;
import com.qikserve.checkout.service.BasketItemService;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.CheckedFunction2;
import io.vavr.control.Try;
import org.apache.commons.lang3.function.Functions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasketControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BasketRepository basketRepository;
    @MockitoBean
    private BasketItemService basketItemService;
    @MockitoBean
    private BasketItemRepository basketItemRepository;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Function<Object, String> toStr = CheckedFunction1.of(mapper::writeValueAsString).unchecked();

    private static final String CONTEXT_PATH = "/baskets";

    @Test
    public void getBasketById_WhenBasketExists_ThenReturnBasket() throws Exception {
        // Given
        var basket = Basket.builder().id(1L).status(BasketStatus.OPEN).build();

        // When
        when(basketRepository.findById(basket.getId())).thenReturn(Optional.of(basket));

        // Then
        this.webTestClient.get().uri(CONTEXT_PATH + "/" + basket.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Basket.class)
                .value(b -> Assertions.assertEquals(toStr.apply(basket), toStr.apply(b)));
    }

    @Test
    public void getBasketById_WhenBasketDoesNotExist_ThenReturnNotFound() throws Exception {
        // Given
        var basketId = 1L;

        // When
        when(basketRepository.findById(basketId)).thenReturn(Optional.empty());

        // Then
        this.webTestClient.get().uri(CONTEXT_PATH + "/" + basketId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

}

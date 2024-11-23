package com.qikserve.checkout.controller;

import com.qikserve.checkout.model.Basket;
import com.qikserve.checkout.model.BasketItem;
import com.qikserve.checkout.model.dto.Savings;
import com.qikserve.checkout.service.BasketService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.nio.file.Path;


@RestController
@RequestMapping("/baskets")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/{basketId}")
    public ResponseEntity<Basket> getBasket(@PathVariable Long basketId){
        return ResponseEntity.of(basketService.getBasket(basketId));
    }

    @PostMapping
    public ResponseEntity<Basket> createBasket(HttpServletRequest request){
        var basket = basketService.createBasket();
        return ResponseEntity.created(Path.of(
                    request.getRequestURI(),
                    basket.getId().toString()).toUri())
                .body(basket);
    }

    @PostMapping("/{basketId}/item")
    public ResponseEntity<BasketItem> addBasketItem(@PathVariable Long basketId, @Valid @RequestBody BasketItem basketItem){
        var item = basketService.addBasketItem(basketId, basketItem);
        return ResponseEntity.created(Path.of(
                    "/basketItems",
                    item.getId().toString()).toUri())
                .body(item);
    }

    @PostMapping("/{basketId}/clear")
    public ResponseEntity<Void> clearBasket(@PathVariable Long basketId){
        basketService.clearBasket(basketId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{basketId}/cancel")
    public ResponseEntity<Void> cancelBasket(@PathVariable Long basketId){
        basketService.cancelBasket(basketId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{basketId}/savings")
        public ResponseEntity<Savings> getBasketSavings(@PathVariable Long basketId){
            var savings = basketService.calculateSavings(basketId);
            return ResponseEntity.ok(savings);
    }

    @PostMapping("/{basketId}/checkout")
    public ResponseEntity<Basket> checkoutBasket(@PathVariable Long basketId){
            Basket finishedBasket = basketService.checkout(basketId);
            return ResponseEntity.ok(finishedBasket);
    }

}

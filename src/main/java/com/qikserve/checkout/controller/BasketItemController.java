package com.qikserve.checkout.controller;

import com.qikserve.checkout.model.BasketItem;
import com.qikserve.checkout.service.BasketItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@RequestMapping("/basketItems")
@RequiredArgsConstructor
public class BasketItemController {

    private final BasketItemService basketItemService;

    @GetMapping("/{basketItemId}")
    public ResponseEntity<BasketItem> getBasketItem(@PathVariable Long basketItemId) {
        return ResponseEntity.of(basketItemService.getBasketItem(basketItemId));
    }

    @PatchMapping("/{basketItemId}")
    public ResponseEntity<BasketItem> updateQuantityBasketItem(@PathVariable Long basketItemId, @RequestBody BasketItem request) {
        return ResponseEntity.ok(basketItemService.updateQuantityBasketItem(basketItemId, request.getQuantity()));
    }

    @DeleteMapping("/{basketItemId}")
    public ResponseEntity<Void> deleteBasketItem(@PathVariable Long basketItemId){
        basketItemService.deleteBasketItem(basketItemId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<BasketItem> createBasketItem(@Valid @RequestBody BasketItem request, HttpServletRequest httpServletRequest){
        var basketItem = basketItemService.createBasketItem(request);
        return ResponseEntity.created(Path.of
                        (httpServletRequest.getRequestURI(),
                        basketItem.getId().toString()).toUri())
                .body(basketItem);
    }

}

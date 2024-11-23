package com.qikserve.checkout.controller;

import com.qikserve.checkout.model.Basket;
import com.qikserve.checkout.service.CheckoutService;
import com.qikserve.checkout.service.dto.CheckoutResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> calculateTotal(@RequestBody Basket basket) {
        try {
            CheckoutResponse response = checkoutService.calculateTotal(basket);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {

            return ResponseEntity.badRequest().body(new CheckoutResponse(null, null, ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CheckoutResponse(null, null, "Error calculating the total."));
        }
    }
}
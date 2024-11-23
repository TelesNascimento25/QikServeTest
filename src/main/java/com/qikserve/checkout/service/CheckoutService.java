package com.qikserve.checkout.service;

import com.qikserve.checkout.model.Basket;
import com.qikserve.checkout.service.dto.CheckoutResponse;

public interface CheckoutService {
    CheckoutResponse calculateTotal(Basket basket);
}
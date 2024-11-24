package com.qikserve.checkout.exception;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.hibernate.annotations.processing.Exclude;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BasketItemNotFoundException extends BasketItemException {
    public BasketItemNotFoundException(Long basketItemId) {
        super(HttpStatus.NOT_FOUND, "error.basketItemNotFound", basketItemId);
    }
}
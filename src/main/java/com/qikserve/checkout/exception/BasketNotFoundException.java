package com.qikserve.checkout.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BasketNotFoundException extends BasketException {
    public BasketNotFoundException(Long basketId) {
        super(HttpStatus.NOT_FOUND, "error.basketNotFound", basketId);
    }
}
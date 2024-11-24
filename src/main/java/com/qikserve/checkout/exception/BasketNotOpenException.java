package com.qikserve.checkout.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BasketNotOpenException extends BasketException {
    public BasketNotOpenException(Long basketId) {
        super(HttpStatus.BAD_REQUEST, "error.basketNotOpen", basketId);
    }
}
package com.qikserve.checkout.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.assertj.core.util.Arrays;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Getter(onMethod_ = {@Override})
public abstract class BasketException extends BaseException implements WithBasketId {
    private final Long basketId;

    protected BasketException(HttpStatus status, String messageCode, Long basketId) {
        super(status, messageCode);
        this.basketId = basketId;
    }

    @Override
    protected Object[] getArgs() {
        return Arrays.array(basketId);
    }
}
package com.qikserve.checkout.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.assertj.core.util.Arrays;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Getter(onMethod_ = {@Override})
public abstract class BasketItemException extends BaseException implements WithBasketItemId {
    private final Long basketItemId;

    protected BasketItemException(HttpStatus status, String messageCode, Long basketItemId) {
        super(status, messageCode);
        this.basketItemId = basketItemId;
    }

    @Override
    protected Object[] getArgs() {
        return Arrays.array(basketItemId);
    }
}
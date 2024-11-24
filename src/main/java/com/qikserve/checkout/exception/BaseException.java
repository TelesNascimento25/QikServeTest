package com.qikserve.checkout.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

@EqualsAndHashCode(callSuper = true)
@Getter(onMethod_ = {@Override})
public abstract class BaseException extends RuntimeException implements WithMessage, WithResponseStatus {
    protected final String messageCode;
    protected final HttpStatus httpStatus;

    protected BaseException(HttpStatus status, String messageCode, Object... args) {
        this.httpStatus = status;
        this.messageCode = messageCode;
    }

    public ErrorResponse toResponse(MessageSource messageSource) {
        return ErrorResponse.create(this, this.httpStatus, this.getMessage(messageSource));
    }

    public String getMessage(MessageSource messageSource) {
        return messageSource.getMessage(messageCode, this.getArgs(), LocaleContextHolder.getLocale());
    }

    protected abstract Object[] getArgs();
}
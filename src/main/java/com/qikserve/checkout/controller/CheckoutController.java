package com.qikserve.checkout.controller;

import com.qikserve.checkout.model.Basket;
import com.qikserve.checkout.model.CheckoutMessages;
import com.qikserve.checkout.model.dto.CheckoutResponse;
import com.qikserve.checkout.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CheckoutController {

  @Autowired private CheckoutService checkoutService;
  @Autowired private MessageSource messageSource;

  @PostMapping("/checkout")
  public ResponseEntity<CheckoutResponse> calculateTotal(@RequestBody Basket basket) {

    try {
      CheckoutResponse response = checkoutService.calculateTotal(basket);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException ex) {
      String errorMessage = messageSource.getMessage("error.productNotFound", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
      return ResponseEntity.badRequest()
                           .body(CheckoutResponse.builder()
                                                 .messages(CheckoutMessages.builder().errorMessage(errorMessage).build())
                                                 .build());

    } catch (Exception ex) {
      String errorMessage = messageSource.getMessage("error.internalServerError", null, LocaleContextHolder.getLocale());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body(CheckoutResponse.builder()
                                                 .messages(CheckoutMessages.builder().errorMessage(errorMessage).build())
                                                 .build());

    }
  }
}

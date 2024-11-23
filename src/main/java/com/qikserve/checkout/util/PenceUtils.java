package com.qikserve.checkout.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PenceUtils {

    public static BigDecimal toPence(BigDecimal value) {
        return value.multiply(BigDecimal.valueOf(100));
    }

    public static BigDecimal toPounds(BigDecimal value) {
        return value.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal toPoundsExact(BigDecimal value) {
        return value.divide(BigDecimal.valueOf(100), RoundingMode.UNNECESSARY);
    }

    public static BigDecimal computeTotal(int quantity, int priceInPence) {
        return BigDecimal.valueOf(priceInPence).multiply(BigDecimal.valueOf(quantity));
    }
}

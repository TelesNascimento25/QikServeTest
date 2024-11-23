package com.qikserve.checkout.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class BuyXGetYFree extends Promotion {
    private Integer requiredQty;
    private Integer freeQty;
}
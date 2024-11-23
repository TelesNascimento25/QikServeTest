package com.qikserve.checkout.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class QtyBasedPriceOverride extends Promotion {
    private Integer requiredQty;
    private Integer price;
}
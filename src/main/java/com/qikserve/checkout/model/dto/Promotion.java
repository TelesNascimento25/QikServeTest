package com.qikserve.checkout.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@RequiredArgsConstructor
@SuperBuilder
@JsonInclude(Include.NON_NULL)
@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        use = JsonTypeInfo.Id.NAME,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BuyXGetYFree.class, name = "BUY_X_GET_Y_FREE"),
        @JsonSubTypes.Type(value = QtyBasedPriceOverride.class, name = "QTY_BASED_PRICE_OVERRIDE"),
        @JsonSubTypes.Type(value = FlatPercent.class, name = "FLAT_PERCENT")
})
public abstract class Promotion {
    private String id;
    private PromotionType type;

    public enum PromotionType {
        BUY_X_GET_Y_FREE,
        QTY_BASED_PRICE_OVERRIDE,
        FLAT_PERCENT
    }
}
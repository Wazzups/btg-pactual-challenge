package com.wazzups.btgpactual.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record OrderEvent(
    @NotBlank(message = "orderId must be provided")
    Long orderId,
    @NotBlank(message = "clientId must be provided")
    Long clientId,
    @NotNull
    List<OrderItem> items){

    public record OrderItem(String productId, int quantity, BigDecimal unitPrice) {}
}

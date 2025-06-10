package com.wazzups.btgpactual.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderEvent(
    Long orderId,
    Long clientId,
    List<OrderItem> items){

    public record OrderItem(String productId, int quantity, BigDecimal unitPrice) {}
}

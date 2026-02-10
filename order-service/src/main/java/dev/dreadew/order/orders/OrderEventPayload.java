package dev.dreadew.order.orders;

import dev.dreadew.order.domain.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderEventPayload {
  Long orderId;
  String customerEmail;
  BigDecimal totalPrice;
  OrderStatus status;
  Instant updatedAt;
}

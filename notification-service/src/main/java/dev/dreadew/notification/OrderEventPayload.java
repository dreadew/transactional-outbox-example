package dev.dreadew.notification;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Data;

@Data
public class OrderEventPayload {
  private Long orderId;
  private String customerEmail;
  private BigDecimal totalPrice;
  private String status;
  private Instant updatedAt;
}

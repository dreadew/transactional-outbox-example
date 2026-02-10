package dev.dreadew.order.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dreadew.order.domain.Order;
import dev.dreadew.order.domain.OrderStatus;
import dev.dreadew.order.outbox.OutboxEvent;
import dev.dreadew.order.outbox.OutboxEventRepository;
import dev.dreadew.order.outbox.OutboxStatus;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private static final String EVENT_TYPE = "order.status.changed";

  private final OrderRepository orderRepository;
  private final OutboxEventRepository outboxEventRepository;
  private final ObjectMapper objectMapper;

  @Transactional
  public Order createOrder(String email, BigDecimal totalPrice) {
    Instant now = Instant.now();
    Order order = new Order();
    order.setCustomerEmail(email);
    order.setTotalPrice(totalPrice);
    order.setStatus(OrderStatus.NEW);
    order.setCreatedAt(now);
    order.setUpdatedAt(now);
    Order saved = orderRepository.save(order);

    enqueueEvent(saved, now);
    return saved;
  }

  @Transactional
  public Order updateStatus(Long orderId, OrderStatus status) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    order.setStatus(status);
    order.setUpdatedAt(Instant.now());
    Order saved = orderRepository.save(order);

    enqueueEvent(saved, Instant.now());
    return saved;
  }

  private void enqueueEvent(Order order, Instant now) {
    OrderEventPayload payload = OrderEventPayload.builder()
        .orderId(order.getId())
        .customerEmail(order.getCustomerEmail())
        .totalPrice(order.getTotalPrice())
        .status(order.getStatus())
        .updatedAt(order.getUpdatedAt())
        .build();

    OutboxEvent event = new OutboxEvent();
    event.setEventType(EVENT_TYPE);
    event.setPayload(serialize(payload));
    event.setStatus(OutboxStatus.NEW.name());
    event.setRetryCount(0);
    event.setAvailableAt(now);
    event.setCreatedAt(now);
    event.setUpdatedAt(now);
    outboxEventRepository.save(event);
  }

  private String serialize(OrderEventPayload payload) {
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Cannot serialize payload", e);
    }
  }

}

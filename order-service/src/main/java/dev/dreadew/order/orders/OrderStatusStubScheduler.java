package dev.dreadew.order.orders;

import dev.dreadew.order.domain.Order;
import dev.dreadew.order.domain.OrderStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusStubScheduler {

  private final OrderRepository orderRepository;
  private final OrderService orderService;

  @Scheduled(fixedDelayString = "${app.orders.stub-status-interval-ms:120000}")
  public void updateStatuses() {
    List<Order> orders = orderRepository.findAll();
    for (Order order : orders) {
      OrderStatus next = nextStatus(order.getStatus());
      if (next != order.getStatus()) {
        orderService.updateStatus(order.getId(), next);
        log.info("Stub status updated for order {} -> {}", order.getId(), next);
      }
    }
  }

  private OrderStatus nextStatus(OrderStatus current) {

    switch (current) {
      case NEW -> {
        return OrderStatus.PAID;
      }
      case PAID -> {
        return OrderStatus.PACKED;
      }
      case PACKED -> {
        return OrderStatus.SHIPPED;
      }
      case SHIPPED -> {
        return OrderStatus.DELIVERED;
      }
      default -> throw new IllegalArgumentException("Unexpected value: " + current);
    }
  }

}

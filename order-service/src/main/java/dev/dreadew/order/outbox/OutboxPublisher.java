package dev.dreadew.order.outbox;

public interface OutboxPublisher {
  void publish(OutboxEvent event);
}

package dev.dreadew.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {

  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
  public void handle(String payload) throws Exception {
    OrderEventPayload event = objectMapper.readValue(payload, OrderEventPayload.class);
    log.info("Send notification for order {} status {}, email {}", event.getOrderId(), event.getStatus(),
        event.getCustomerEmail());
  }

}

# Order Service

Сервис заказов, который демонстрирует паттерн Transactional Outbox.

## Что делает сервис

- Создает заказ через REST.
- В той же транзакции пишет запись в `outbox_events`.
- Шедулер читает outbox и публикует события в Kafka.
- STUB: каждые 2 минуты автоматически обновляет статус заказов.

## Основные файлы

- `src/main/java/dev/dreadew/order/orders/OrderController.java` — REST endpoint `POST /orders`.
- `src/main/java/dev/dreadew/order/orders/OrderService.java` — бизнес-логика + запись в outbox.
- `src/main/java/dev/dreadew/order/outbox/OutboxProcessor.java` — обработка outbox + ретраи.
- `src/main/java/dev/dreadew/order/orders/OrderStatusStubScheduler.java` — STUB статусов.
- `src/main/resources/application.yaml` — настройки БД/Kafka.

## Запуск

Из корня репозитория:

```bash
make infra
make order
```

## Проверка

```bash
curl -X POST http://localhost:8081/orders \
  -H 'Content-Type: application/json' \
  -d '{"customerEmail":"demo@dreadew.dev","totalPrice":1200}'
```

После этого можно проверить:

- таблицу `orders`
- таблицу `outbox_events`
- логи `order-service`

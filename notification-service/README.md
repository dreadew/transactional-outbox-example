# Notification Service

Сервис уведомлений — получает события из Kafka и пишет в лог.

## Что делает сервис

- Слушает топик `order-events`.
- Десериализует payload из JSON.
- Логирует «отправку уведомления».

## Основные файлы

- `src/main/java/dev/dreadew/notification/NotificationListener.java` — Kafka listener.
- `src/main/java/dev/dreadew/notification/OrderEventPayload.java` — DTO события.
- `src/main/resources/application.yaml` — настройки Kafka.

## Запуск

Из корня репозитория:

```bash
make infra
make notification
```

## Проверка

1. Запустите `order-service` и создайте заказ.
2. В логах этого сервиса появится сообщение о полученном событии.

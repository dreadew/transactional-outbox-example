# Пример Transactional Outbox

Этот каталог содержит полностью рабочий пример из статьи:

- `order-service` — сервис заказов, пишет события в `outbox_events` в одной транзакции с заказом.
- `notification-service` — сервис уведомлений, слушает Kafka и логирует события.
- `docker-compose.yaml` — Postgres 16 + Kafka (KRaft).

## Что нужно установить

- Java 17
- Maven
- Docker + Docker Compose

## Быстрый запуск

1. Поднять инфраструктуру:

```bash
make infra
```

2. Запустить сервисы (в отдельных терминалах):

```bash
make order
make notification
```

3. Создать заказ:

```bash
curl -X POST http://localhost:8081/orders \
  -H 'Content-Type: application/json' \
  -d '{"customerEmail":"demo@dreadew.dev","totalPrice":1200}'
```

## Что должно произойти

- `order-service` создает запись в `orders` и в `outbox_events`.
- Каждые 2 минуты (STUB) статус заказа обновляется и формируется новое событие.
- `notification-service` получает события из Kafka и пишет лог.

## Полезные команды

- Создать заказ (curl):

```bash
make create-order
```

- Проверить сообщения в Kafka (последние 5):

```bash
make check-kafka
```

- Проверить таблицу `outbox_events`:

```bash
make check-outbox
```

- Остановить инфраструктуру и удалить данные:

```bash
make infra-down
```

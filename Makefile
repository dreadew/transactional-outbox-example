.PHONY: infra infra-down order notification create-order check-kafka check-outbox

infra:
	docker compose up -d postgres kafka

infra-down:
	docker compose down -v

order:
	mvn -f pom.xml -pl order-service spring-boot:run

notification:
	mvn -f pom.xml -pl notification-service spring-boot:run

create-order:
	curl -X POST http://localhost:8081/orders \
	  -H 'Content-Type: application/json' \
	  -d '{"customerEmail":"demo@dreadew.dev","totalPrice":1200}'

check-kafka:
	docker compose -f docker-compose.yaml exec -T kafka \
	  /opt/kafka/bin/kafka-console-consumer.sh \
	  --bootstrap-server localhost:9092 \
	  --topic order-events \
	  --from-beginning \
	  --max-messages 5

check-outbox:
	docker compose -f docker-compose.yaml exec -T postgres \
	  psql -U order_user -d orders \
	  -c "select id, event_type, status, retry_count, available_at, created_at from outbox_events order by id desc limit 10;"

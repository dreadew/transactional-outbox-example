package dev.dreadew.order.outbox;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select e from OutboxEvent e where e.status in :statuses and e.availableAt <= :now order by e.createdAt")
  List<OutboxEvent> findReady(@Param("statuses") List<String> statuses, @Param("now") Instant now);

}

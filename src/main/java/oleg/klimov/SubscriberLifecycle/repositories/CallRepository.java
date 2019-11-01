package oleg.klimov.SubscriberLifecycle.repositories;

import oleg.klimov.SubscriberLifecycle.entities.Call;
import oleg.klimov.SubscriberLifecycle.entities.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CallRepository extends JpaRepository<Call,Long> {
    @Query("select count(c) FROM Call c WHERE c.date = ?1 AND c.sender = ?2")
    int countCallsPerDay(LocalDate date, Subscriber sender);
}

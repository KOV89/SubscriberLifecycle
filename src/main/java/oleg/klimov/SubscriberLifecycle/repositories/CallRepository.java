package oleg.klimov.SubscriberLifecycle.repositories;

import oleg.klimov.SubscriberLifecycle.entities.Call;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRepository extends JpaRepository<Call,Long> {
}

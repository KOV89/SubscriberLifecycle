package oleg.klimov.SubscriberLifecycle.repositories;

import oleg.klimov.SubscriberLifecycle.entities.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    Subscriber findByUsername(String Username);
}

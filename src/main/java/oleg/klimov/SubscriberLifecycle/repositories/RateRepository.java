package oleg.klimov.SubscriberLifecycle.repositories;

import oleg.klimov.SubscriberLifecycle.entities.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate,Long> {
    Rate findById(long i);
}

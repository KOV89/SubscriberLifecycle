package oleg.klimov.SubscriberLifecycle.repositories;

import oleg.klimov.SubscriberLifecycle.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {
}

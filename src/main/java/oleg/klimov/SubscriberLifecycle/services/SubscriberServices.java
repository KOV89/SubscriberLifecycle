package oleg.klimov.SubscriberLifecycle.services;

import oleg.klimov.SubscriberLifecycle.repositories.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SubscriberServices implements UserDetailsService {
    private final SubscriberRepository subscriberRepository;

    public SubscriberServices(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return subscriberRepository.findByUsername(s);
    }
}

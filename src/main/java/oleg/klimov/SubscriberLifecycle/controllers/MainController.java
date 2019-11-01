package oleg.klimov.SubscriberLifecycle.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import oleg.klimov.SubscriberLifecycle.entities.*;
import oleg.klimov.SubscriberLifecycle.exceptions.ForbiddenException;
import oleg.klimov.SubscriberLifecycle.exceptions.NotFoundException;
import oleg.klimov.SubscriberLifecycle.repositories.CallRepository;
import oleg.klimov.SubscriberLifecycle.repositories.MessageRepository;
import oleg.klimov.SubscriberLifecycle.repositories.SubscriberRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@RestController
public class MainController {
    private final SubscriberRepository subscriberRepository;
    private final MessageRepository messageRepository;
    private final CallRepository callRepository;

    public MainController(SubscriberRepository subscriberRepository, MessageRepository messageRepository, CallRepository callRepository) {
        this.subscriberRepository = subscriberRepository;
        this.messageRepository = messageRepository;
        this.callRepository = callRepository;
    }

    @GetMapping("/subscriberInfo/{msisdn}")
    @JsonView(Views.Subscriber.class)
    public Subscriber getSubscriberInfo(@PathVariable(name = "msisdn") Subscriber subscriber) {
        if (subscriber==null)
            throw new NotFoundException("Subscriber not found");
        return subscriber;
    }

    @PutMapping("/payment")
    @JsonView(Views.Subscriber.class)
    public Subscriber payment(@RequestBody Map<String,String> map) {
        Subscriber subscriber=subscriberRepository.findByUsername(map.get("subscriber"));
        if (subscriber==null)
            throw new NotFoundException("Subscriber not found");
        subscriber.setBalance(subscriber.getBalance()+Float.parseFloat(map.get("pay")));
        if (subscriber.getBalance()>0) subscriber.setActive(true);
        subscriberRepository.save(subscriber);
        return subscriber;
    }

    @PostMapping("/message")
    @JsonView(Views.Message.class)
    public Message newMessage(@RequestBody Map<String,String> map) {
        Subscriber sender=subscriberRepository.findByUsername(map.get("sender"));
        Subscriber recipient=subscriberRepository.findByUsername(map.get("recipient"));
        if (sender==null || recipient==null)
            throw new NotFoundException("Subscriber not found");
        if (sender.isActive()) {
            sender.setBalance(sender.getBalance() - sender.getRate().getCostMessage());
            if (sender.getBalance()<0) sender.setActive(false);
            subscriberRepository.save(sender);
            Message message = new Message(map.get("text"), LocalDateTime.now(), sender, recipient);
            messageRepository.save(message);
            return message;
        }
        else throw new ForbiddenException("Negative balance. Top up your balance");
    }

    @PostMapping("/call")
    @JsonView(Views.Call.class)
    public Call newCall(@RequestBody Map<String,String> map) {
        Subscriber sender=subscriberRepository.findByUsername(map.get("sender"));
        Subscriber recipient=subscriberRepository.findByUsername(map.get("recipient"));
        if (sender==null || recipient==null)
            throw new NotFoundException("Subscriber not found");
        if (sender.isActive()) {
            if (callRepository.countCallsPerDay(LocalDate.now(),sender) < sender.getRate().getCallLimit()) {
                sender.setBalance(sender.getBalance() - sender.getRate().getCostCall());
                if (sender.getBalance()<0) sender.setActive(false);
                subscriberRepository.save(sender);
                Call call = new Call(LocalDate.now(),LocalTime.now(),sender,recipient);
                callRepository.save(call);
                return call;
            }
            else throw new ForbiddenException("Daily call limit exceeded");
        }
        else throw new ForbiddenException("Negative balance. Top up your balance");
    }
}

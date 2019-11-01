package oleg.klimov.SubscriberLifecycle.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import oleg.klimov.SubscriberLifecycle.entities.*;
import oleg.klimov.SubscriberLifecycle.exceptions.ForbiddenException;
import oleg.klimov.SubscriberLifecycle.exceptions.NotFoundException;
import oleg.klimov.SubscriberLifecycle.repositories.CallRepository;
import oleg.klimov.SubscriberLifecycle.repositories.MessageRepository;
import oleg.klimov.SubscriberLifecycle.repositories.SubscriberRepository;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@RestController
public class MainController {
    private static final Logger logger = Logger.getLogger(MainController.class);
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
    public Subscriber getSubscriberInfo(@PathVariable(name = "msisdn") String s) {
        Subscriber subscriber=subscriberRepository.findByUsername(s);
        if (subscriber==null) {
            logger.info("Subscriber " + s + " not found");
            throw new NotFoundException("Subscriber not found");
        }
        logger.info("Information provided about "+s);
        return subscriber;
    }

    @PutMapping("/payment")
    @JsonView(Views.Subscriber.class)
    public Subscriber payment(@RequestBody Map<String,String> map) {
        Subscriber subscriber=subscriberRepository.findByUsername(map.get("subscriber"));
        if (subscriber==null) {
            logger.info("Subscriber " + map.get("subscriber") + " not found");
            throw new NotFoundException("Subscriber not found");
        }
        subscriber.setBalance(subscriber.getBalance()+Float.parseFloat(map.get("pay")));
        if (subscriber.getBalance()>0) subscriber.setActive(true);
        subscriberRepository.save(subscriber);
        logger.info("Subscriber " + subscriber.getMsisdn() + " replenished the balance by " + map.get("pay"));
        return subscriber;
    }

    @PostMapping("/message")
    @JsonView(Views.Message.class)
    public Message newMessage(@RequestBody Map<String,String> map) {
        Subscriber sender=subscriberRepository.findByUsername(map.get("sender"));
        Subscriber recipient=subscriberRepository.findByUsername(map.get("recipient"));
        if (sender==null) {
            logger.info("Sender " + map.get("sender") + " not found");
            throw new NotFoundException("Sender not found");
        }
        if (recipient==null) {
            logger.info("Recipient " + map.get("recipient") + " not found");
            throw new NotFoundException("Recipient not found");
        }
        if (sender.isActive()) {
            sender.setBalance(sender.getBalance() - sender.getRate().getCostMessage());
            if (sender.getBalance()<0) sender.setActive(false);
            subscriberRepository.save(sender);
            Message message = new Message(map.get("text"), LocalDateTime.now(), sender, recipient);
            messageRepository.save(message);
            logger.info("Subscriber " + sender.getMsisdn() + " sent a message to " + recipient.getMsisdn());
            return message;
        }
        else {
            logger.info("Failed to send message. Subscriber " + sender.getMsisdn() + " has a negative balance");
            throw new ForbiddenException("Negative balance. Top up your balance");
        }
    }

    @PostMapping("/call")
    @JsonView(Views.Call.class)
    public Call newCall(@RequestBody Map<String,String> map) {
        Subscriber sender=subscriberRepository.findByUsername(map.get("sender"));
        Subscriber recipient=subscriberRepository.findByUsername(map.get("recipient"));
        if (sender==null) {
            logger.info("Sender " + map.get("sender") + " not found");
            throw new NotFoundException("Sender not found");
        }
        if (recipient==null) {
            logger.info("Recipient " + map.get("recipient") + " not found");
            throw new NotFoundException("Recipient not found");
        }
        if (sender.isActive()) {
            if (callRepository.countCallsPerDay(LocalDate.now(),sender) < sender.getRate().getCallLimit()) {
                sender.setBalance(sender.getBalance() - sender.getRate().getCostCall());
                if (sender.getBalance()<0) sender.setActive(false);
                subscriberRepository.save(sender);
                Call call = new Call(LocalDate.now(),LocalTime.now(),sender,recipient);
                callRepository.save(call);
                logger.info("Subscriber " + sender.getMsisdn() + " called " + recipient.getMsisdn());
                return call;
            }
            else {
                logger.info("Failed to call. Subscriber " + sender.getMsisdn() + " exceeded the daily calls limit");
                throw new ForbiddenException("Daily call limit exceeded");
            }
        }
        else {
            logger.info("Failed to call. Subscriber " + sender.getMsisdn() + " has a negative balance");
            throw new ForbiddenException("Negative balance. Top up your balance");
        }
    }
}

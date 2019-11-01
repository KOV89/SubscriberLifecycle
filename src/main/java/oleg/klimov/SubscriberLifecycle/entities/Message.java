package oleg.klimov.SubscriberLifecycle.entities;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonView(Views.Message.class)
    private String text;
    @JsonView(Views.Message.class)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "sender_msisdn", referencedColumnName = "msisdn")
    @JsonView(Views.Message.class)
    private Subscriber sender;

    @ManyToOne
    @JoinColumn(name = "recipient_msisdn", referencedColumnName = "msisdn")
    @JsonView(Views.Message.class)
    private Subscriber recipient;

    public Message() {
    }

    public Message(String text, LocalDateTime time, Subscriber sender, Subscriber recipient) {
        this.text = text;
        this.time = time;
        this.sender = sender;
        this.recipient = recipient;
    }

    public String getText() {
        return text;
    }

    public Long getId() {
        return id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Subscriber getSender() {
        return sender;
    }

    public void setSender(Subscriber sender) {
        this.sender = sender;
    }

    public Subscriber getRecipient() {
        return recipient;
    }

    public void setRecipient(Subscriber recipient) {
        this.recipient = recipient;
    }

}
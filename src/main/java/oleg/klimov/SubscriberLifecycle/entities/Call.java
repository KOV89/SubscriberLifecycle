package oleg.klimov.SubscriberLifecycle.entities;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "calls")
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonView(Views.Call.class)
    private LocalDate date;
    @JsonView(Views.Call.class)
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "sender_msisdn", referencedColumnName = "msisdn")
    @JsonView(Views.Call.class)
    private Subscriber sender;

    @ManyToOne
    @JoinColumn(name = "recipient_msisdn", referencedColumnName = "msisdn")
    @JsonView(Views.Call.class)
    private Subscriber recipient;

    public Call() {
    }

    public Call(LocalDate date, LocalTime time, Subscriber sender, Subscriber recipient) {
        this.date = date;
        this.time = time;
        this.sender = sender;
        this.recipient = recipient;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
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

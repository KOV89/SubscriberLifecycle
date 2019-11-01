package oleg.klimov.SubscriberLifecycle.entities;

import javax.persistence.*;

@Entity
@Table(name = "rates")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Float costCall;

    private Integer callLimit;

    private Float costMessage;

    public Rate() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getCostCall() {
        return costCall;
    }

    public void setCostCall(Float costCall) {
        this.costCall = costCall;
    }

    public Integer getCallLimit() {
        return callLimit;
    }

    public void setCallLimit(Integer callLimit) {
        this.callLimit = callLimit;
    }

    public Float getCostMessage() {
        return costMessage;
    }

    public void setCostMessage(Float costMessage) {
        this.costMessage = costMessage;
    }
}

package oleg.klimov.SubscriberLifecycle.entities;

public final class Views {
    public interface Subscriber {
    }
    public interface Message extends Subscriber {
    }
    public interface Call extends Subscriber {
    }
}
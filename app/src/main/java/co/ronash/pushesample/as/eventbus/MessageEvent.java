package co.ronash.pushesample.as.eventbus;


/**
 * To send event through app from a place to another place, we use `EventBus`.
 * This class will be the object that will be sent and received.
 */
public class MessageEvent {

    private String message;

    public String getMessage() {
        return message;
    }

    public MessageEvent(String message) {
        this.message = message;
    }
}
